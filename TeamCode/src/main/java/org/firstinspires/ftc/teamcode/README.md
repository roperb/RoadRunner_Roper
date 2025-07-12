## Welcome!

This is a simple readme file with some resources.

## How to code as a team

Each team member on their computer should have a copy of the code cloned onto it.
Any new features will be made on a branch to keep things separate from each other.

A branch is a separate version of the code that you can work on separate from the main version.
As new features are added, the original code is left unchanged.  We can even push code with
the branches onto Github.

After the code has been tested thoroughly and no issues are there, we can merge the code locally
and then push the locally tested code up to Github.

First we want to make sure that any features that are on Github have been fetched and merged locally.
    `git checkout master`
    `git fetch origin`
    `git merge origin/master`
Then we want to merge the branch to the local master.
    `git merge <branch-name>`
Build the code and test it again.

Then we can push it to Github using the push tool.

## How to update when the SDK releases a new version.

First check out to see if the roadrunner quickstart has updated to the new version.
This file is built off of that repository rather than the original SDK

Check out the [official Roadrunner docs](https://rr.brott.dev/docs/v1-0/tuning/).

As of 7/12/25 the SDK version is 10.3 and this project is at version 10.3

Following [this](https://ftc-docs.firstinspires.org/en/latest/programming_resources/tutorial_specific/android_studio/fork_and_clone_github_repository/Fork-and-Clone-From-GitHub.html) website as a guide we will do a "fetch" and "merge":

1. Make sure you are in the master branch in the terminal
    `git branch`
2. Then to gather the changes but not yet merge them, in the terminal, type:
    `git fetch upstream`
3. Then merge the fetched changes
    `git merge upstream/master`
4. If  you then need to merge these to the branch you are working on, type:
    `git checkout <feature-branch>`
    `git merge master`

If you have issues, check the linked guide for how to revert commits 
or downgrade the SDK to a different version.  If all else fails, we can
always start from scratch, re-fork the repository, and copy code that
we know works.
