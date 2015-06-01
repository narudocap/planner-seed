# planner-seed

Further testing is required: only 3 test cases have been used

Known issues: 
- The final plan may contain multiple StartC / StopC actions, only the first one should be included, the rest should be discarded.

Basic structure of the code:
- The planner package contains classes for the elements of a cloud application (VM, component, Binding, port). 
- The CloudApp class gathers the main functionality (functions diff to calculate differences between two cloud apps, actions to provide a sequence of high-level actions to go from an app configurtion to another, and genPlan to generate a concrete plan to go from one app conf to another)
- Both actions and genPlan deal with Action objects (defined in package planner.actions). Each action class provides an implementation of the performAction method, which implements the effect of performing such action on an app. 
