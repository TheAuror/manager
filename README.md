# Financial manager
This is a plain and simple program to help you manage your financial situation.

# Features
You can create your user account.
You can create or edit your incomes and expenses, which can be periodical.
For example you can create a record which represents your salary and set the period to a month, and the date to the 10th
of the month.
After this on the 10th of each month your salaries record will be created.

# Usage
To use the program, use the following command in the project base directory:

	mvn package

After that you run the program by executing the generated .jar file in the target folder:

	java -jar manager-1.0-SNAPSHOT-jar-with-dependencies.jar

# Generate site
To generate the site for the project run the following command in the project base directory:

	mvn site

The generated site and the reports will be placed in the target/site/ folder.

To generate test coverage run:

	mvn clover:instrument clover:clover

The generated reports will be placed under /target/site/clover.