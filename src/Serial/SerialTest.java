package Serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;

import com.ma.cmms.api.client.MaCmmsClient;
import com.ma.cmms.api.client.dto.MeterReading;
import com.ma.cmms.api.crud.AddRequest;

public class SerialTest implements SerialPortEventListener
{
	SerialPort serialPort;

	private static MaCmmsClient client;
	private static long assetID;
	private static long unitID;

	private static final String[] PORT_NAME = new String[1];

	private BufferedReader input;
	private String handler;

	private static final int TIME_OUT = 30000;
	private static final int DATA_RATE = 9600;

	public SerialTest(MaCmmsClient c, long AID, long UID, String WPort)
	{
		client = c;
		assetID = AID;
		unitID = UID;
		PORT_NAME[0] = WPort;
	}

	//Runs the program
	@SuppressWarnings("rawtypes")
	public void initialize()
	{
		SerialPortReader.setLabel("<html><center>Running...<br>Started.</center></html>");

		//The next line is for Raspberry Pi and gets us into the while loop and was suggested here:
		//  http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		//Uncomment it for Raspberry Pi devices.

		//System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;

		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			for (String portName : PORT_NAME)
			{
				if (currPortId.getName().equals(portName))
				{
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null)
		{
			SerialPortReader.setLabel("<html><center>Running...<br>Error: Could not find a serial port.</center></html>");
			return;
		}

		SerialPortReader.setLabel("<html><center>Running...<br>Found serial port: " + portId.getName() + "</center></html>");

		try
		{
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			//set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			//open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

			//add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		}
		catch (Exception e)
		{
			SerialPortReader.setLabel("<html><center>Running...<br>Error: " + e.toString() + "</center></html>");
		}
	}

	//Prevents port locking. Call at program closing.
	public synchronized void close()
	{
		if (serialPort != null)
		{
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	//Handle an event on the serial port. Read the data and send it to the CMMS.
	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				handler = input.readLine();
				addMeterRd();
			}
			catch (Exception e)
			{
				if (!e.toString().equals("java.io.IOException: Underlying input stream returned zero bytes"))
				{
					SerialPortReader.setLabel("<html><center>Running...<br>Error: " + e.toString() + "</center></html>");
				}
			}
		}
	}

	//Method to send data to the CMMS
	public synchronized void addMeterRd()
	{
		try
		{
			AddRequest<MeterReading> addReq = client.prepareAdd(MeterReading.class);

			MeterReading newMR = new MeterReading();
			newMR.setIntAssetID(assetID);
			newMR.setDblMeterReading(Double.parseDouble(handler));
			newMR.setDtmDateSubmitted(new Date());
			newMR.setIntMeterReadingUnitsID(unitID);

			addReq.setObject(newMR);
			addReq.setFields("id, intAssetID, dblMeterReading, dtmDateSubmitted, intMeterReading");

			client.add(addReq);

			SerialPortReader.setLabel("<html><center>Running...<br>Last Meter Reading: " + handler + "</center></html>");
		}
		catch (Exception e)
		{
			SerialPortReader.setLabel("<html><center>Running...<br>Meter Reading " + handler + " Missed at " + new Date() + "</center></html>");
		}
	}
}