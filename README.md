# reqbot-web

[![Build Status](https://travis-ci.org/typingincolor/reqbot-web.svg?branch=master)](https://travis-ci.org/typingincolor/reqbot-web)

## What is it?

[reqbot](http://github.com/typingincolor/reqbot) is a programmable mock backend to allow you test your application is calling a web api correctly.

You can tell reqbot what response you want when your client calls the api, all from inside your unit or integration tests.

reqbot-web is a web front-end to reqbot

## Running reqbot-web

There are a number of options, but you will need redis running whichever you choose.

### Gradle

use `gradle run`

This will start everything at `http://localhost:8080/`

### Foreman

Foreman can be install from [here](http://blog.daviddollar.org/2011/05/06/introducing-foreman.html)
 
To use it `gradle stage; foreman start`
 
This will start everything at `http://localhost:5000/` by default.

Foreman is useful as it uses the Procfile which heroku uses if you deploy there.

## Environment

You can tell reqbot-web where the reqbot API is located. This can be done using the application.yml file or the `REQBOT_API_URL` environment variable.

If you use foreman, the port that reqbot-web listens on can be set using the `PORT` environment variable.
