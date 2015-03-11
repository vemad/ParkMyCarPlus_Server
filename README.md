# PMC_Server
The application PMC_Server is a Java Spring Rest Server to use with [PMC_Android](https://github.com/OT-SIMS-ParkMyCar/PMC_Android).

## How to setup
You will need:
- Git
- Java JRE 1.7
- Java JDK 1.7
- Graddle
- MySQL

### On Ubuntu

##### Install tools
Start with a global update of the system:
```sh
sudo apt-get update
```
Install Git:
```sh
sudo apt-get install git
```
Install Java JRE:
```sh
sudo apt-get install default-jre
```
Install Java JDK:
```sh
sudo apt-get install default-jdk
```
Install Gradle:
```sh
add-apt-repository ppa:cwchien/gradle
apt-get update
apt-get install gradle
```
Install MySQL:
```sh
sudo apt-get install mysql-server
```
##### Download the project
When all tools are installed, you can clone the Git Repository:
```sh
sudo mkdir /home/OT-SIMS
cd /home/OT-SIMS
sudo git clone https://github.com/OT-SIMS-ParkMyCar/PMC_Server.git
```

##### Initialize the Database
First be sure that the service is started `sudo service mysql start`.

To initialize MySQL, you most be connected as root
`mysql --user=root mysql`
If you have assigned a password to the root account, you also need to supply a `--password` or `-p` option.

Create the database and tables by using __setupMySQL.sql__ file from the Git repository with the mysql command: 
```sh
source /home/OT-SIMS/PMC_Server/toSetup/setupMySQL.sql
```
If you want insert some data in the database to test use the file __datasMySQL.sql__.
```sh
source /home/OT-SIMS/PMC_Server/toSetup/datasMySQL.sql
```
It's recommended to don't use the root user for the application so you most create one with rights on the pmc database:
```sh
GRANT SELECT, INSERT, UPDATE, DELETE ON pmc.* TO 'pmcServer'@'localhost' IDENTIFIED BY 'pmcServerPwd';
```
If you want previously remove all table from PMC, use this command:
```
DROP TABLE `logplace`, `place`, `user`, `zone`, `favorite`;
```
The default user for the application is __pmcServer__ with the password __pmcServerPwd__ but you can change that in the file __/home/OT-SIMS/PMC_Server/src/main/java/com/pmc/config/AppConfig.java__.

You can now disconnect from MySQL with the commmand `exit`.

##### Build the project
To build the project, run the command graddle build:
```sh
cd /home/OT-SIMS/PMC_Server
sudo gradle build
```

You can run the server with the command `gradle run`.
To stop it, use `Ctrl+C`.

##### Create a service for the server
To create the service, you must use the file __PMC_Server_service.txt__
```sh
sudo cp /home/OT-SIMS/PMC_Server/toSetup/PMC_Server_service.txt /etc/init.d/PMC_Server
sudo chmod 775 /etc/init.d/PMC_Server
```

Run `sudo /etc/init.d/PMC_Server start` to start the service.
Show logs with `tail -f /var/log/PMC_Server.log`.
Run `sudo /etc/init.d/PMC_Server stop` to stop the service.

