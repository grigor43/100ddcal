100ddcal
====

Dashboard for Humana 100 Day Dash.

Hacking
----
1. Checkout code
2. Hack
3. Send a Pull Request

`H100DD.groovy` is where team names are set. If you want to add your team to the dashboard, do so here.

This app is built using [Gaelyk](http://gaelyk.appspot.com/). To launch a local instance, run

```
./gradlew appengineRun
```

To load data from the production instance, run 

```
./bootstrap.sh
```