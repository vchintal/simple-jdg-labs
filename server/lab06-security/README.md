## lab06-security (server)

### Prerequisite

The JDG server(s) should be started in domain mode and should be ready before the lab is executed. Please refer to the documentation [here](../../README.md) to start the JDG server(s) in domain mode.

### Lab Description

This lab demonstrates how to work with a remote **JBoss Data Grid** cache that is secured with well defined roles and requires authentication from the HotRod client upon connection.  Refer to the [documentation](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/administration_and_configuration_guide/#red_hat_jboss_data_grid_security_authorization_and_authentication) for more details. 

There is a significant change on the both the client side and the server side (configuration) to enable authentication. 

On the server side:
1. The cache container should have a security and authorization enabled that also lists all the possible roles and their cache permissions
2. The cache that needs to be secured should have security and authorization enabled that lists all the roles that can be applied. The role names that need to be mentioned should be a subset of what was mentioned at the cache container level
3. Authentication should be enabled on the **hotrod-connector** with applicable SASL mechanisms 

On the client side:
1. A SASL callback handler needs to be defined 
2. Security/authentication needs to be enabled on the RemoteCacheManager configuration along with an instance of callback handler with user credentials for authentication    

### Executing the lab

#### Setup cache(s)

The instruction to deploy the `secureCache` and all additional server side configuration is in the file [addCaches](cli/addCaches.cli). To execute the CLI (command line interface) script, execute the command as shown below

```sh 
# Run from the root folder of the lab
$JDG_HOME/bin/cli.sh -c --file=cli/addCaches.cli
```

This will deploy the cache configuration in the file **demo-domain.xml** as shown below:

```xml
            <subsystem xmlns="urn:infinispan:server:core:8.4">
                <cache-container name="clustered" default-cache="default" statistics="true">
                    <transport lock-timeout="60000"/>
                    <security>
                        <authorization>
                            <identity-role-mapper/>
                            <role name="writer" permissions="WRITE READ BULK_WRITE BULK_READ"/>
                            <role name="reader" permissions="READ BULK_READ"/>
                            <role name="admin" permissions="ADMIN"/>
                        </authorization>
                    </security>
                    ...
                    <distributed-cache-configuration name="secure-cache-configuration">
                        <security>
                            <authorization enabled="true" roles="reader writer"/>
                        </security>
                    </distributed-cache-configuration>
                    <distributed-cache name="secureCache" configuration="secure-cache-configuration"/>
                </cache-container>
            </subsystem>                
            ...
            <subsystem xmlns="urn:infinispan:server:endpoint:8.1">
                <hotrod-connector cache-container="clustered" socket-binding="hotrod">
                    <topology-state-transfer lazy-retrieval="false" lock-timeout="1000" replication-timeout="5000"/>
                    <authentication security-realm="ApplicationRealm">
                        <sasl mechanisms="DIGEST-MD5" qop="auth" server-name="hotrodserver"/>
                    </authentication>
                </hotrod-connector>
                <memcached-connector cache-container="clustered" socket-binding="memcached"/>
                <rest-connector cache-container="clustered" socket-binding="rest">
                    <authentication security-realm="ApplicationRealm" auth-method="BASIC"/>
                </rest-connector>
            </subsystem>
```

#### Run client application

After you have perused the client code thoroughly, run the following command to execute the client code.

```sh 
mvn clean compile exec:exec
```

### Output

You should see the following output on the node:

```sh
[INFO] --- exec-maven-plugin:1.5.0:exec (default-cli) @ lab06-security ---
12:29:06.301 [main] INFO  org.everythingjboss.jdg.server.JDGRemoteClientConsoleApp - Total number of entries writter to the secure cache is 1000
```

### Testing/Verification

Follow the instuctor's guidance on how to verify if the security as defined with working with the `secureCache`.

Use the following commands to create three users:

```sh 
$JDG_HOME/bin/add-user.sh -a -u dgreader -p dgreader1! -g reader
$JDG_HOME/bin/add-user.sh -a -u dgwriter -p dgwriter1! -g writer
$JDG_HOME/bin/add-user.sh -u admin -p redhat1! -g admin
```

Now run the following tests:

1. Use the `dguser` credentials in the HotRod client and see if the inserts go thru
2. Change the user now to `dgwriter` and verify again
3. Repeat step #1 and comment out the insert code and apply a cache reading code and verify **READ** access 

#### Outcomes

1. The test #1 above should fail citing improper permissions
2. The test #2 should go thru as expected
3. The test #3 should also work where only cache reading is performed


