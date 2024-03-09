# Mastodon-Dynamo-App

(For better viewing, you can visit: https://github.com/aitanagoca/Mastodon-Dynamo-App)

## Group Information 

👥 Group: (P102, grup 05)

Aitana González (U186651, Bucket: lsds2024.lab3.output.u186651)

Jordi Alfonso (U111792, Bucket: lsds2024.lab3.output.u111792) 

Arnau Royo (U172499, Bucket: lsds2024.lab3.output.u172499)

## (For group mates) - How to execute

⚠️ If you’re having troubles running the application locally, with errors similar to ”cannot find method methodName()”, it might be due to jar conflicts between spark and the dependencies of your applica- tion. Find your Spark installation, move to the jar directory (in downloaded spark, the jars directory; in brew spark, the libexec/jars directory, etc.) and remove the following files: gson-2.2.4.jar (or equivalent versions), okhttp-3.12.12.jar (or equivalent versions), okio-1.14.0.jar (or equivalent versions).

### (PART 2) Running example application locally

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonStreamingExample target/lab3-mastodon-1.0-SNAPSHOT.jar src/main/resources/map.tsv

### (PART 3) Stateless: joining a static RDD with a real time stream

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonStateless target/lab3-mastodon-1.0-SNAPSHOT.jar src/main/resources/map.tsv

### (PART 4) Spark Stateful transformations with windows

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonWindows target/lab3-mastodon-1.0-SNAPSHOT.jar src/main/resources/map.tsv

### (PART 5) Spark Stateful transformations with state variables

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonWithState target/lab3-mastodon-1.0-SNAPSHOT.jar en


