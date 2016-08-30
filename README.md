Serial Port Reader v1.0
-----------------------

### Table Of Contents ###

1. [Authors And Contributors](#1-authors--contributors)  
2. [Introduction](#2-introduction)  
3. [Requirements](#3-requirements)  
4. [Running](#4-running)  
5. [Troubleshooting](#5-troubleshooting)  
  5.1 [Credentials](#51-credentials)  
  5.2 [Running... Started Stall](#52-running-started-stall)  
  5.3 [Running... Errors](#53-running-errors)  
6. [Licensing](#6-licensing)  
7. [Change Log](#7-change-log)  
  [v1.0.1](#v101)  
  [v1.0.0](#v100)  

<br>


### 1. Authors &amp; Contributors ###

[v1.0.0](#100) Created June 19, 2015 by Jake Uskoski  
[v1.0.1](#101) Created Aug 25,  2016 by Jake Uskoski  

[Back to Top](#serial-port-reader-v10)
<br>


### 2. Introduction ###

The Serial Port Reader is a java application meant to listen to a single serial
port for the collection of meter readings from a device such as an Arduino
board.

It can only be linked to a single asset, and it cannot have multiple types of
meter readings being fed at once. For example, if the serial port is recieving
data for both a light sensor and a temperature sensor, both will be read and
given the same unit, instead of two different units for the two types of
sensors.

For a simple step-by-step guide to setting up this program, see the file named
"Step-By-Step Beginner Guide.txt".

[Back to Top](#serial-port-reader-v10)
<br>


### 3. Requirements ###

This program requires the Java SE Runtime Environment 8, and uses the Fiix
client for Java, which depends on the following external libraries:

* Apache Commons Codec
* Apache Commons Logging
* Apache Jakarta HttpClient
* Jackson JSON processor

These libraries, along with the Fiix CMMS client for Java, are packaged in the
"lib" folder.

This program also requires the RXTX library, which is available online at:

&nbsp;&nbsp;&nbsp;[http://rxtx.qbang.org/wiki/index.php/Download][RXTX]

There are different steps required depending on the operating system of your
computer. Instructions for installing the RXTX library are included in the
download. The binary download is for easy installation, while the source version
is for when it is required you build the library yourself.

  [RXTX]: http://rxtx.qbang.org/wiki/index.php/Download

[Back to Top](#serial-port-reader-v10)
<br>


### 4. Running ###

The "SerialPortReader.jar" file is an executable jar, and can be run by
double clicking.

For steps on how to operate the program, see the Step-By-Step Beginner
Guide.txt" file.

[Back to Top](#serial-port-reader-v10)
<br>


### 5. Troubleshooting ###

Some of the errors are presented to the user through text in the window, but
The errors can be unclear.

[Back to Top](#serial-port-reader-v10)
<br>


#### 5.1 Credentials ####

If the "Set Credentials" button causes nothing to happen, then the URL entered
in the first field is not a valid URL. Check to make sure that "https://" is at
the beginning of the URL.

If the "Set Credentials" button causes an error to appear, one of the following
issues has occurred:

1. The credentials are wrong.
2. The API application is Inactive on the CMMS.
  * Check the API Applications under the API Application Settings in the Connect
    Management menu.
3. The server could not be reached.
4. There is no internet connection.

[Back to Top](#serial-port-reader-v10)
<br>


#### 5.2 Running... Started Stall ####

If the program stays stuck at "Running... Started." and never begins showing the
last meter reading taken, there is a problem, and it is possible one of the
following issues has occurred:

1. The Java 8 SE Runtime Environment has not been set up properly.
  * Try reinstalling the Java 8 SE Runtime Environment
  * Try downloading the Java 8 SE Development Kit instead.
    It includes the Java 8 SE Runtime Environment, and has less trouble
    installing on Mac computers.
  * Make sure you are using the Java 8 SE Runtime Environment, and not an
    older runtime environment
2. The RXTX library has not been set up properly.
  * Make sure the RXTXcomm.jar file is in the proper file folder.
  * Make sure the other file (depending on which operating system) is in the
    proper folder.
3. The file[s] in the installer are not compatible with your operating system.
  * This can occur with Macs, where the librxtxSerial.jnilib file is not correct
    for a specific operating system.
    * A version of the file that is correct for an OS X 10.7.5 can be found at [http://blog.iharder.net/wp-content/uploads/2009/08/librxtxSerial.jnilib][lib]
    * For operating systems more recent than the OS X 10.7.5, building the
      library from the source code may be necessary. There are instructions in
      the source download on how to build the library from the source code for
      each operating system.


If none of the above worked, then download the [Eclipse IDE for Java EE Developers][Luna],
install it, download the Java 8 SE Developer Kit and install it, and import the
folder where this program was extracted to as an existing project. At this point
it is assumed you have programming and debugging experience, eclipse knowledge,
and can discover, and fix, the issue yourself. Please report the issue, if
discovered, to the most recent contributor of this program, detailing what is
wrong, as well as your operating system, and if solved, the solution to be added
to the troubleshooting section.

  [lib]: http://blog.iharder.net/wp-content/uploads/2009/08/librxtxSerial.jnilib
  [Luna]: http://www.eclipse.org/downloads/

[Back to Top](#serial-port-reader-v10)
<br>


#### 5.3 Running... Errors ####

If the following error occurs:

&nbsp;&nbsp;&nbsp;"Error: Could not find a serial port."

Then take the following steps:

1. Make sure the device sending data to the serial port is connected and
   operational.
  * If the device is an Arduino, make sure that there wasn't an error with
    uploading the sketch. If the Arduino at some poit was reset or disconnected
    from the computer, then this program was be shut down and reopened after the
    Arduino has been reconnected and gotten its sketch reuploaded.
2. Make sure the serial port entered was correct.
  * If the device is an Arduino, the serial port is displayed in the bottom
    right corner of the Arduino program for writing and uploading sketches, and
    is also viewable through the "Tools" menu.


If, after the "Running... Started." message, the following error appears:

&nbsp;&nbsp;&nbsp;"Error: gnu.io.PortInUseExecption"

and you are using a Mac OS X, then there is most likely an issue with the
folders that the RXTX library is attempting to read, causing it to believe the
port is in use by another program and is being locked out from accessing it.

To remedy the situation, open the terminal and navigate to the root folder of
your computer. This can be done by entering:

    cd ..

repeatedly until you can no longer go out of a folder. typing:

    ls

and hitting enter will show a list of folders, one of which should be "Users".
You're now in the root folder. Use the following command to create the directory
the RXTX library is looking for:

    sudo mkdir /var/lock

You will be asked for your password, since this is doing things in the root
folder. Your password will not appear as you type, but will be checked once you
hit enter. Next, enter the following command to give the folder read/write
permissions for the user:

    sudo chmod 777 /var/lock

This will most likely not require a password, since it was just entered and is
assumed that you are still the same person.

If any other unusual errors with no explanation or an unclear explanation occur,
please send an e-mail to the most recent contributor of this program detailing
the issue and how it can be reproduced.

[Back to Top](#serial-port-reader-v10)
<br>


### 6. Licensing ###

The Serial Port Reader and Fiix client for Java are licensed under the Apache
License 2.0.

See [LICENSE.txt][LI1], [NOTICE.txt][NTC], and [RXTX-LICENSE.txt][LI2] files for
more information.

  [LI1]: LICENSE.txt
  [LI2]: RXTX-LICENSE.txt
  [NTC]: NOTICE.txt

[Back to Top](#serial-port-reader-v10)
<br>


### 7. Change Log ###

#### v1.0.1 ####
* Changes to documentation and program to reflex rebranding

[Back to Top](#serial-port-reader-v10)

#### v1.0.0 ####
* Initial creation of the project
* Began hosting on GitHub
* Created the program and executable jar
* Created a "README.md"
* Created a "Step-By-Step Beginner Guide.txt"
* Created an example arduino sketch
* Added the licensing statements

[Back to Top](#serial-port-reader-v10)
