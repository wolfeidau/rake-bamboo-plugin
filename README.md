# DEPRECATION NOTICE

I don't want to maintain this plugin anymore since I don't use bamboo at all now, and rarely write Ruby code, and even less so Java code.

I have had this plugin archived in the marketplace, hopefully this frees up space for someone else to start a new plugin for ruby.

# Ruby Bamboo Plugin

This is a bamboo plugin project which when installed enables configuration of tasks which run various ruby based build tools including bundler, rake, and capistrano.

## Features
Via a Bamboo task, this plugin enables you to run:
* Bundler
* Rake
* Capistrano

## Usage

To use this Bamboo plugin you will need a ruby runtime.  This can either be the one installed by default on OSX or one installed
via package manager in for example Ubuntu. If your build server is not running as root, which is preferable, you will need
to install all the gems used by your project prior to running a build.


## Bundler

#### Bundler Install
The `Bundler Install` task by default is setup to quickly execute `bundle install` dependencies during the build process. Note will only work when RVM is installed in the home directory of the user the build server is running under.

#### Bundler CLI
The `Bundler CLI` is setup to allow for any variations on command line execution of `bundler`

#### Bundle exec with Rake
The `Rake` task has the option to be run from `bundle exec`


## RVM Support

Alternatively you could [install RVM](http://rvm.io) and use a separate gemset for your project.

As an example follow the following steps to setup a project and run some tests using this plugin. This assumes your using
either OSX or Linux. If your on OSX you will need to install XCode, and if your installing version 4.1 then you will
also need [GCC Installer](https://github.com/kennethreitz/osx-gcc-installer/downloads) as this version omits gcc in favor
of llvm and clang.

1. [Install RVM](http://rvm.io)  (once installed it may list some dependencies you need to install).

        $ curl -L https://get.rvm.io | bash -s stable

2. Install a ruby.


        rvm install 1.9.3


3. Switch to using this ruby.


        rvm use 1.9.3


4. Create a new gemset for rails.


        rvm gemset create rails3


5. Switch to this gemset.


        rvm use 1.9.3@rails3


6. Install the rails gem and rspec


        gem install rails rspec-rails


7. Build a new rails project

        rails bamboo-build-test -T

8. Add RSpec to project by appending this line to the bottom of the Gemfile in the newly created project.

        gem "rspec-rails", :group => [:test, :development]

9. Run bundler to ensure all the dependencies are installed into the gemset you created.

        bundle install

9. Configure rspec in this project.

        rails generate rspec:install

10. Using scaffold build a data entry interface for a model.

        rails generate scaffold Post name:string title:string content:text

11. Remove the two pending specs, not necessary for this example.

        rm ./spec/helpers/posts_helper_spec.r ./spec/models/post_spec.rb

12. Run the database migration script.

        rake db:migrate

13. Run rspec to ensure all the generated tests, should be all green.

        rspec spec

14. Add this project to your favourite online project hosting site.
15. Navigate to the capability admin interface and click the "Auto Detection" link, this should detect the installed
ruby and it's associated gemsets.
16. Create a plan which uses the online project site and add a job with a Rake Task which runs the spec target using the 1.9.3@rails3 runtime.

## Rbenv Support

This plugin should pickup ruby runtimes installed using rbenv, I use a subset of the rvm steps to test this.

## Test Reporting

### RSpec
To enable test reporting do as follows.

1. Add the this fragment to your Gemfile.

```ruby
group :test do
	gem "rspec_junit_formatter"
end
```

2. Edit your the .rspec file in the base of your project and replace the contents with.

```
--format RspecJunitFormatter
--out rspec.xml
```

3. Add a JUnit test task to your Job and configure it to look for rspec.xml which contains the test results.

### Cucumber
Edit the `config/cucumber.yml` and change the `std_opts` to include the `junit` formatter as well as specifiy the output directory. For example:

```
std_opts = "-r features/support/ -r features/step_definitions --quiet -f pretty -f junit -o test-reports --strict --tags ~@wip --tags ~@todo"
```

## Ruby Runtime Manager Support

In version 1.5 I added support for more than one ruby version manager. The ones which were initially supported are:

* System Ruby Runtime Manager which will search /usr/bin and /usr/local/bin for ruby and gem commands. Note it is done this way to avoid PATH overrides in the default shell, commonly done by developers or RVM.
* RVM Runtime Manager.
* rbenv Runtime Manager.
* Windows Runtime Manager.

To enable this feature a concept of runtime labels was introduced, these are in the form of ruby runtime manager then ruby version and gemset (for those without gemsets everything it is set to default) for example 'RVM 1.9.3-p0@rails32'.

When the plugin encounters a runtime label which has no ruby runtime manager as with existing installations it will just use RVM.

## System Ruby Support

This will search /usr/bin and /usr/local/bin for ruby and gem commands. Note it is done this way to avoid PATH overrides in the default shell, commonly done by developers or RVM.

## Windows Support

As of version 2.0 I added a ruby runtime manager which supports detection of ruby(s) on windows systems.

1. I test this using the [rails installer](http://railsinstaller.org/) project installed on the system. On my test system I verified there was only on installation of ruby.exe using the which command.

```
Microsoft Windows [Version 6.1.7601]
Copyright (c) 2009 Microsoft Corporation.  All rights reserved.

C:\Users\markw>where ruby.exe
C:\RailsInstaller\Ruby1.9.3\bin\ruby.exe

C:\Users\markw>
```

2. I have also tested [jruby](http://jruby.org/) 1.7 on windows the only caveat is this must be added to the %PATH%.

*Note:* updates the %PATH% variable you will require you to restart your bamboo server.

I have tested this with a simple gem project built with bundler and a rails 3.2 project.

## Links

* [RSpec JUnit XML Formatter](https://github.com/sj26/rspec_junit_formatter)
* [RVM](http://rvm.io/)
* [bamboo](http://www.atlassian.com/software/bamboo/overview)
* [rake](http://martinfowler.com/articles/rake.html)
