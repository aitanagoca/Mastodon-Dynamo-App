# Mastodon-Dynamo-App

(For better viewing, you can visit: https://github.com/aitanagoca/Mastodon-Dynamo-App)

## Group Information 

👥 Group: (P102, grup 05)

Aitana González (U186651)

Jordi Alfonso (U111792) 

Arnau Royo (U172499)

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

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonWithState target/lab3-mastodon-1.0-SNAPSHOT.jar en

### (PART 6) DynamoDB

#### (PART 6.1) Writing to Dynamo DB

⚠️ Before following these steps, remember the aws configuration!! (1️⃣ aws configure; 2️⃣ aws configure set aws_session_token < your_aws_session_token >)

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonHashtags target/lab3-mastodon-1.0-SNAPSHOT.jar en

#### (PART 6.2) Writing from Dynamo DB

⚠️ Before following these steps, remember the aws configuration!! (1️⃣ aws configure; 2️⃣ aws configure set aws_session_token < your_aws_session_token >)

1️⃣ Mvn: mvn clean

2️⃣ Mvn: mvn validate

3️⃣ Mvn: mvn compile

4️⃣ Mvn: mvn package

5️⃣ Mvn: spark-submit --conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///log4j.properties --class edu.upf.MastodonHashtagsReader target/lab3-mastodon-1.0-SNAPSHOT.jar en

## Output

### (PART 2) Running example application locally

From the output, we can conclude that the application is functioning correctly. It’s successfully capturing tweets and their associated data in real-time. The data includes the tweet’s content, the user who posted it, and any hashtags used.

<img width="1189" alt="Captura de pantalla 2024-03-09 a les 17 22 14" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/a4bf01b4-668a-421f-975c-3c3bc06c8dbc">

### (PART 3) Stateless: joining a static RDD with a real time stream

From the output, we can conclude that the application is functioning correctly. It’s successfully capturing tweets and their associated languages in real-time. The data includes the language of the tweet and the count of tweets in that language. English appears to have the highest number of tweets in both time intervals displayed.

<img width="309" alt="Captura de pantalla 2024-03-14 a les 14 28 20" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/ec87c44c-c5a7-4746-944b-c56045e22965">

<img width="309" alt="Captura de pantalla 2024-03-14 a les 14 28 30" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/f6400dce-e9e5-442d-8123-ee9fdb0f87cf">

### (PART 4) Spark Stateful transformations with windows

From the output, we can conclude that the application is functioning correctly. It’s successfully capturing tweets and their associated languages in real-time. The data includes the language of the tweet and the count of tweets in that language. English appears to have the highest number of tweets in both the micro batch and the 60-second window.

<img width="313" alt="Captura de pantalla 2024-03-14 a les 15 22 48" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/e0b8b1b9-776e-4784-be17-5fcfb55c3f6c">

<img width="313" alt="Captura de pantalla 2024-03-14 a les 15 23 04" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/c570e9f4-98fd-488b-b528-b30c60ccf99b">

<br>

<img width="313" alt="Captura de pantalla 2024-03-14 a les 15 23 37" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/bd9cbedc-6fd8-4e98-8480-c6af265e4aff">

<img width="313" alt="Captura de pantalla 2024-03-14 a les 15 23 50" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/ca219867-8749-432b-b160-0b7a94cd3112">

### (PART 5) Spark Stateful transformations with state variables

From the output, we can conclude that the application is functioning correctly. It’s successfully capturing users and their associated number of toots in real-time. The data includes the user’s name and the count of toots they have made. The users are sorted by the number of toots they have made, with the user having the most toots listed first.

<img width="383" alt="Captura de pantalla 2024-03-14 a les 14 33 09" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/5dbb0a21-bf05-47f7-88aa-019404d3cfa5">

<img width="383" alt="Captura de pantalla 2024-03-14 a les 14 33 19" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/e5c05208-c145-494a-96c5-d9a1d1294ada">

### (PART 6) DynamoDB

#### (PART 6.1) Writing to Dynamo DB

Partial example after writing to DynamoDB table "LsdsTwitterHashtags":

<img width="739" alt="Captura de pantalla 2024-03-13 a les 22 24 29" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/f569ae89-825c-4023-b454-85ac16f0c9f3">

From the output, we can conclude that the Spark streaming application is successfully extracting hashtags from tweets and storing the data in DynamoDB. The data includes the frequency of each hashtag, the language of the tweet, and the tweet IDs where the hashtag appears. This information could be useful for analyzing trends in hashtag usage.

#### (PART 6.2) Writing from Dynamo DB

Example of obtained top 10 after reading from DynamoDB table "LsdsTwitterHashtags": 

<img width="228" alt="Captura de pantalla 2024-03-13 a les 22 27 31" src="https://github.com/aitanagoca/Mastodon-Dynamo-App/assets/92036724/48010445-4167-491a-baa2-7ec3192e19a7">

