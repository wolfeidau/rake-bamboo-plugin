# Introduction

This document explains how I have setup RVM on my build host.

# RVM Install

For ubuntu which I use as my build server environment I chose to install rvm in /usr/local this enables all users including the one which tomcat is running under to access it. To setup ubuntu the same way do as follows:

1. Firstly sudo to a root shell and install the main dependencies.

        $ sudo -i
        # apt-get install git-core curl

2. Now as we are building ruby from sources, using RVM we will need a development suite and the dependencies needed by this software.

        # apt-get install build-essential
        # apt-get install zlib1g-dev libssl-dev libreadline-dev libxml2-dev libxslt1-dev sqlite3 libsqlite3-dev

3. Next we install rvm using the handy new url.

        # curl -L get.rvm.io | bash -s stable

4. Now we install a ruby runtime.

        # rvm install 1.9.3

5. Then we are ready to setup a gemset and bootstrap some gems.

        # rvm use 1.9.3
        # rvm gemset create rails32
        # rvm use 1.9.3@rails32

6. Next we have the two options, we can install all the gems needed by our project this will essentially freeze the libraries our project is using, or we can give the build server sudo access which inturn will enable bundler to work.

# Option 1

1. Install bundler and rake.

        # gem install rake bundler

2. Drop back to a user on the server and check out your project.

        $ git clone http://github/yourname/yourapp
        $ cd yourapp

3. Tell rvm to setup the environment for your ruby@gemset combination.

        $ rvm use 1.9.3@rails32

4. Run bundle install.

        $ bundle install

NOTE: You will be prompted to sudo at this point to install the gems, this is something built into bundler.

# Option 2

1. As root run visudo and at the end of the file add the following:

        # This permits running of SUDO without a TTY
        Defaults:tomcat6 !requiretty

        # This enables it to cp / mv stuff from its stage directory to gems directory.
        # Need to look at constraining this a bit to a specific path ideally.
        %rvm ALL=NOPASSWD: ALL

2. Create .bundle directory in tomcat6 users home directory and give it permission to write to that path.

        mkdir /usr/share/tomcat6/.bundler
        chown tomcat6:tomcat6 /usr/share/tomcat6/.bundler

NOTE: This method is a security RISK as the build server now has the permission to SUDO and do anything, I need to do more search on how to reduce this risk.
