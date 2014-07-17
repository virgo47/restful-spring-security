restful-spring-security
=======================

Small test RESTful app with token based security. Its main reason is documentation for myself. :-) This demo features:

* `@RestController`s in separate MVC application context.
* Main application context with `@Service`s and Spring Security (also parent for MVC context).
* Security filter... TODO
* Gradle build with Spring IO Platform "bill of materials" (but Spring Boot repackage is disabled).

**Demo does not feature any front-end JavaScript. Sorry.** You have to use browser, preferably with something like: http://restclient.net/

Demo is inspired by internal needs, took a lot of information from Google and StackOverflow - which lead me to: https://github.com/philipsorst/angular-rest-springsecurity/
That project has additional AngularJS and JPA, while I wanted to focus on Spring Security + MVC's @RestController only + practice Gradle a bit. 

Notes:

* If `context:component-scan` is used in Spring configs, be sure to specify disjoint values for `base-package`. You don't want your Controllers to be picked
  by main appcontext or other way around. Separate JARs don't solve this as the resolution (initialization) is performed during runtime.