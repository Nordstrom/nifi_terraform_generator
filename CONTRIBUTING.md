# Contributing

Thank you so much for taking the time to contribute! Below is some information that you need to contribute effectively.

## Development

This repository contains a utility to generate terraform scripts for a given NIFI `flow.xml`.  
Development of this plugin can be done from any of your favorite IDE which supports Java & Maven.

To build, run:

`mvn clean install`

## Testing

To run tests:

`mvn test`

To print Jacoco coverage from target folder after running tests:

`awk -F"," '{ instructions += $4 + $5; covered += $5 } END { print covered, "/", instructions, "instructions covered"; print 100*covered/instructions, "% covered" }' target/site/jacoco/jacoco.csv`

## Patches/Fixes 

All patches/fixes are welcome. To contribute a patch:

* Break your work into small, single-purpose patches if possible. It's much harder to merge in a large change with a lot of disjoint features.
* Create an issue on Github
* Submit the patch as a GitHub pull request against the master branch.
* Make sure that your code passes the unit tests.
* Add new unit tests for your code.

## Have a shiny new feature?

To request/add a new feature to the codebase:

* Create a issue on github
* If no one is working on it, assign it to yourself. Fork repo, raise a PR against master.
* Make sure that your code passes the unit tests.
* Add new unit tests for your code. 
