Goal
====
Produce a simple web-app backend to complement the supplied front-end code.

# Notes

## Build and run the application

To build and run the backend prerequisites are:
- NPM and Gulp like Robee & Rinchen mentioned :)
- Java8 SDK
- PostgreSQL

By default the db will be `expenses` at `localhost:5432` with username `postgres` and password `pass`.

If needed, update `gradle.properties` and `hibernate-local.cfg.xml` to change them.

Many Gradle tasks are availabe:

- `resetDb` will create (dropping it before if already present) the db
- `flywayMigrate` will run all migrations
- `build` will build the project
- `buildUI` will build the ui (calling `gulp dev`)
- `shadowJar` will create the final jar file

To completely build and prepare the application, run:
```bash
./gradlew clean resetDb flywayMigrate build shadowJar
```

After a successful build, the app can be run with:
```bash
java -jar build/libs/expenses-1.0-SNAPSHOT-all.jar
```
Then you can start use the application from the url: [http//localhost:8080/](http//localhost:8080/)

## Notes on tech stack and libraries

### Spark Java
I've never used it for production, but I wanted something lightweight and simple without too many configurations.

### Lombok
Even if I don't like having too many annotations I found Lombok useful to avoid some boilerplate. 

I just try to not put `@Getter` and `@Setter` everywhere just because it's easy.

As a general rule I never use builders in production code, only in tests. Unfortunately there is no way to enforce it with Lombok.

### Jackson
It's what I use every day, still lots of annotations but less code to write.

### Flyway Migrations
I've tried others in the past, but I like this one better, especially with the ability to write migrations in Java when needed.
 
### PostgreSQL
I've used MySQL (MariaDb) in the past, I slightly prefer PostgreSQL. 
Mainly because of JSON support, schemas and subquery performance.  

### Hibernate
I have mixed feelings about it... 

I like it when I have a complex model to map and many relationships 
between entities. The ability to persist the root entity and having everything stored in cascade saves a lot of headaches.

I hate it when the model I want to model is different from the table structure and when I need to perform "complex" queries.
Also too many annotaions.

### No Spring
I've decided not to use Spring or any DI library because of the size of the application and to try to limit over-engineering.

### Vavr (formerly know as Javaslang)
At work we have a custom library with classes similar to `Validation` and `Either` in Vavr.

I use them to have a consistent way of managing validation problems and errors, avoiding as much as possible to
throw exceptions.

### AssertJ
I prefer the API and the error reporting of this library instead of plain JUnit with Hamcrest.

### No mocking library
I use Mockito in my job, but I prefer to just create manually the couple of mocks and stubs I needed here.

## Notes on user stories

#### User story 1
There is quite a big thing missing in the UI: there is no concept of _User_, so expenses are not associated to any of them,
making the entire application a lot less useful.

I assumed this was to keep the challenge simple and since it would need some work in the UI I left it.

#### User story 2
No note here.

#### User story 3
It came at no cost, so it will work not only for _EUR_ currency but also for: _AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK, HKD, 
HRK, HUF, IDR, ILS, INR, JPY, KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON, RUB, SEK, SGD, THB, TRY, USD, ZAR_.

Basically all currencies supported by `http://fixer.io/`.

Here I would have suggested to implement it in a different way, I don't think it's a good idea to convert 
the amount and just store it in _GBP_.

I would have stored the amount and the currency inserted by the user in the DB along with the rate 
on the given date.

|    date    | amount | currency | rate |
|:----------:|:------:|:--------:|:----:|
| 2017-06-20 |  15.00 |    EUR   | 1.41 |

In that way:
- if later we want to support multiple currencies we already have all the data we need
- we can always calculate all amounts in _GBP_
- we can decide to show the original amount and currency
- if the call to the currency converter fails we can store the amount without the rate, show it in the 
original currency and retry to fetch the rate later. We won't be so tied to the availability of the 3rd party API.


The task
--------------


Imagine that you come back from 2 weeks of holidays on a Monday. On the team scrum board, assigned to you, two tasks await :


**User story 1:**

> **As a user, i want to be able to enter my expenses and have them saved for later.**

> _As a user, in the application UI, I can navigate to an expenses page. On this page, I can add an expense, setting :_

> 1. _The date of the expense_
> 0. _The value of the expense_
> 0. _The reason of the expense_

> _When I click "Save Expense", the expense is then saved in the database._
> _The new expense can then be seen in the list of submitted expenses._


**User story 2:**

> **As a user, I want to be able to see a list of my submitted expenses.**


> _As a user, in the application UI, i can navigate to an expenses page. On this page, I can see all the expenses I already submitted in a tabulated list.
> On this list, I can see :_

> 1. _The date of the expense_
> 0. _The VAT (Value added tax) associated to this expense. VAT is the UK’s sales tax. It is 20% of the value of the expense, and is included in the amount entered by the user._
> 0. _The reason of the expense_
>

By email, the front end developer of the team let you know that he already worked on the stories,  did build an UI and also went on holidays to France!

>_"Hi backEndDeveloper,_

>_Hope you had nice holidays.
>I did create an UI and prepared resources calls for those 2 user stories.
>You should only have to create the right endpoints in your service for the frontend application to call and everything should work fine!...
>Unless I forgot something of course, in which case you may be able to reach me on the beach_
>
>_PS. You can start the frontend by running `gulp` this will compile the code and launch a webserver on `localhost:8080`. You are free to host the files in your backend of course, then you will only need to build the source by running `gulp dev`. If you want to have a look at the code that is calling the endpoints then you can find this in `src/js/apps/codingtest/expenses/expenses-controller.js`_
>
>_PS II. In case you are stuck, you need to prep the project with `npm install -g gulp && npm install`. I'll leave it to you how to get Node on your system ;-)_
>
>_A bientôt !_
>
> _Robee_ & _Rinchen_
>"

Mandatory Work
--------------

Fork this repository. Starting with the provided HTML, CSS, and JS, create a Java-based REST API that:

1. Provides your solution to user story `1` and user story `2`
0. Alter the README to contain instructions on how to build and run your app.

Give our account `engagetech` access to your fork, and send us an email when you’re done. Feel free to ask questions if anything is unclear, confusing, or just plain missing.

Extra Credit
------------


_All the following work is optional. The described tasks do not need to be fully completed, nor do they need to be done in order.
You could chose to do the front-end part of a story, or the backend one, or only an endpoint of the backend one for example.
You could pick one to do completely or bits and pieces of the three, it is up to you as long as you explain to us what you did (or didn't) chose to do._


You finished way in advance and can't wait to show your work at Wednesday's demo session. But you decide to impress the sales team a bit more and go back to the team kanban board.
There you find some extra unassigned user stories :


**User story 3:**

> **As a user, I want to be able to save expenses in euros**

> _As a user, in the UI, when I write an expense, I can add the chars_ `EUR` _after it (example : 12,00 EUR).
> When this happens, the application automatically converts the entered value into pounds and save the value as pounds in the database.
The conversion needs to be accurate. It was decided that our application would call a public API to either realise the conversion or determine the right conversion rate, and then use it._

**User story 4:**

>**As a user, I want to see the VAT calculation update in real time as i enter my expenses**

> _After conversation with the dev team, we decided that the VAT should be calculated client-side as the user enters a new expense, before they save the expense to the database._
> _Robee being on holidays, Can I assign that to you backEndDeveloper?_


Questions
---------
##### What frameworks can I use?
That’s entirely up to you, as long as they’re OSS. We’ll ask you to explain the choices you’ve made. Please pick something you're familiar with, as you'll need to be able to discuss it.

##### What application servers can I use?
Anyone you like, as long as it’s available OSS. You’ll have to justify your decision. We use dropwizard and Tomcat internally. Please pick something you're familiar with, as you'll need to be able to discuss it.

##### What database should I use?
MySQL or PostgreSQL. We use MySQL in-house.

##### What will you be grading me on?
Elegance, robustness, understanding of the technologies you use, tests, security.

##### Will I have a chance to explain my choices?
Feel free to comment your code, or put explanations in a pull request within the repo. If we proceed to a phone interview, we’ll be asking questions about why you made the choices you made.

##### Why doesn’t the test include X?
Good question. Feel free to tell us how to make the test better. Or, you know, fork it and improve it!
