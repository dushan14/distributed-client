# distributed-client

<b>To create the jar, from project root</b> 

If you have installed maven use  
  ```mvn clean install -DskipTests```    

otherwise use  
  ```./mvnw clean install -DskipTests``` on unix based os  
  ```./mvnw.cmd clean install  -DskipTests``` on windows  

<b>To run</b>  
```
java -jar target/dsapp.jar  
```

<b>Application commands</b>

| Command                          | Description                         | Example              |
| -------------------------------- |-------------------------------------|----------------------|
| `reg ip_of_bootstrap_server`     | register to bootstrap server (bs)   | `reg 192.168.43.139` |
| `regl`                           | register to bs in same ip           |                      |
| `unreg`                          | unregister from bootstrap server    |                      |
| `join`                           | sending join commands to neighbours |                      |
| `reqgossip`                       | requesting gossips from neighbours |                      |
| `leave`                          | sending leave commands to neighbours|                      |
| `table`                          | show routing table                  |                      |
| `files`                          | show selected files                 |                      |
| `search file_name hops[optional]`| file search from neighbours, hops to go naighbours of neighbour | `search windows` |
| `download file_download_url`     | download a file from neighbour      | `download http://10.10.6.35:8082/download?name="Windows XP"` |
| `exit`                           | exit from application followed by 'unreg' and 'leave' |    |
| `help`                           | show application commands           |                      |
| `setport port`                   | hange port if registration failed   | `setport 5656`       |
| `setnodelimit nodes`             | set maximum nodes limit in routing table | `setnodelimit 5`|
| `nodelimit`                      | show maximum nodes limit            |                      |
| `sethops hops`                   | set maximum hops count              | `sethops 3`          |
| `hops`                           | show maximum hops count             |                      |
| `stat`                           | show counts of queries              |                      |
| `clearstat`                      | set counts of queries to 0          |                      |

