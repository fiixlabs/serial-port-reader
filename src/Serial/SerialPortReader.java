package Serial;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.ma.cmms.api.client.BasicCredentials;
import com.ma.cmms.api.client.MaCmmsClient;
import com.ma.cmms.api.rpc.RpcRequest;
import com.ma.cmms.api.rpc.RpcResponse;

@SuppressWarnings("serial")
public class SerialPortReader extends JFrame implements ActionListener, WindowListener
{
	private long assetID;
	private long unitID;

	public static JPanel panel;
	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	private JTextField field4;
	private JButton buttonSet;
	private JButton buttonRun;
	public static JLabel label;

	private SerialTest main;
	private BasicCredentials credentials;
	private MaCmmsClient client;

	public SerialPortReader()
	{
		initUI();
	}

	public static void setLabel(String output)
	{
		label.setText(output);
		panel.revalidate();
		panel.repaint();
	}

	private void initUI()
	{
		panel = new JPanel();

		field1 = new JTextField("API URL", 20);
		field2 = new JTextField("API Application Key", 20);
		field3 = new JTextField("API Access Key", 20);
		field4 = new JTextField("API Secret Key", 20);

		buttonSet = new JButton("Set Credentials");
		buttonRun = new JButton("Begin Program");
		label = new JLabel();

		buttonSet.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				credentials = new BasicCredentials(field2.getText(), field3.getText(), field4.getText());
				client = new MaCmmsClient(credentials, field1.getText());

				ping();
			}
		});

		buttonRun.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean flag = false;
				try
				{
					assetID = Long.parseLong(field1.getText(), 10);
					unitID = Long.parseLong(field2.getText(), 10);
				}
				catch (NumberFormatException err)
				{
					flag = true;
				}

				if (flag != false)
				{
					label.setText("<html><center>Error: One or more IDs aren't numbers.</center></html>");
				}
				else
				{
					main = new SerialTest(client, assetID, unitID, field3.getText());

					label.setText("<html><center>Running...</center></html>");

					field1.setEditable(false);
					field2.setEditable(false);
					field3.setEditable(false);
					panel.remove(buttonRun);

					panel.revalidate();
					panel.repaint();

					main.initialize();
				}
			}
		});

		panel.add(field1);
		panel.add(field2);
		panel.add(field3);
		panel.add(field4);
		panel.add(buttonSet);
		panel.add(label);

		add(panel);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				int confirm = JOptionPane.showOptionDialog(null,
						"Are you sure you want to exit the program?",
						"Exit Confirmation",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						null);
				if (confirm == 0)
				{
					try
					{
						main.close();
					}
					catch (NullPointerException nullErr)
					{
						System.exit(0);
					}
					System.exit(0);
				}
			}
		});

		setTitle("Serial Port Reader");
		setSize(new Dimension(260, 200));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//setResizable(false);
		setLocationRelativeTo(null);
	}

	public void ping()
	{
		RpcRequest rpcReq = client.prepareRpc();
		rpcReq.setName("Ping");

		RpcResponse rpcResp = client.rpc(rpcReq);
		if (rpcResp.getError() != null)
		{
			label.setText("<html><center>Error: " + rpcResp.getError().getLeg().toString() + "</center></html>");
			panel.revalidate();
			panel.repaint();
		}
		else
		{
			field1.setText("Asset ID");
			field2.setText("Unit ID");
			field3.setText("Serial Port");
			label.setText("");

			panel.remove(field4);
			panel.remove(buttonSet);

			panel.add(buttonRun);

			panel.revalidate();
			panel.repaint();
		}
	}

	public static void main(String[] args)
	{

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				SerialPortReader ex = new SerialPortReader();
				ex.setVisible(true);
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}
}