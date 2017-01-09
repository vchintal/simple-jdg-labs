## Simple JDG 7.0 Labs

The goal of these labs is to be able to introduce fundamental and core features of enterprise caching thru **JBoss Data Grid 7.0** quickly via simple labs that does not require knowledge of anything beyond core java (JDK 8+).

### Prerequisites

#### System Requirements

1. At least 4 GB of available RAM
2. At least 10 GB of available disk space
3. At least 2 CPU cores (the more the better) 

#### Software Requirements
1. [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * Ensure that **JAVA_HOME** environment variable is set
   * Ensure that **$JAVA_HOME/bin** is on the **PATH** so that you can run **java** on command line at prompt from any folder
2. [Maven 3.3](https://maven.apache.org/download.cgi)
3. Any IDE for Java Development (for example [Eclipse](https://eclipse.org/downloads/), [IntelliJ IDEA](https://download-cf.jetbrains.com/idea/ideaIC-2016.3.2.tar.gz)    )  or ***preferably*** [JBoss Developer Studio](https://developers.redhat.com/products/devstudio/download/) 
4. [JBoss Data Grid 7.0 Server](https://developers.redhat.com/download-manager/file/jboss-datagrid-7.0.0-server.zip)

### Labs Setup

The labs are provided to you in two flavors **embedded** and **server**. Following is the list of labs in both flavors:


There is no additional setup requirement for **library** mode labs but for working with **server** mode labs, you have to install and ready the **JDG Server**. 

#### JDG Server Setup

1. Unzip the server distribution file in a location of your choice. Lets refer to the installed path including the folder **jboss-datagrid-7.0.0-server** as **$JDG_HOME**. That is, you should be able to change directory to **$JDG_HOME/bin**.
2. Add an administrative user with the following command:

   ```sh
   $JDG_HOME/bin/add-user.sh -u admin -p redhat1! 
   ```

3. Navigate to folder `$JDG_HOME/domain/configuration` and run the following commands:

   ```sh
   cp domain.xml demo-domain.xml 
   cp host.xml demo-host.xml
   ```

4. When it is time to work with the server (in domain mode), run the following command:

   ```sh
   $JDG_HOME/bin/domain.sh --domain-config=demo-domain.xml --host-config=demo-config.xml
   ```   
