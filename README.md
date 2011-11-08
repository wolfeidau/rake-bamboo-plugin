# Overview

This is a bamboo plugin project which when installed enables configuration of rake build tasks. It interfaces with
ruby version manager (RVM) to enable execution against different ruby versions.

# Goals

* Discover ruby installations managed by RVM and list them in bamboo.
* Perform a rake build based on selection of a ruby runtime and entry of some build targets.
* Parse the output of rspec2 run and display a table of results.

# Links

* [RVM](http://beginrescueend.com/)
* [bamboo](http://www.atlassian.com/software/bamboo/overview)
* [rake](http://martinfowler.com/articles/rake.html)
