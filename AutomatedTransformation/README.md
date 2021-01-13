# Amalthea To RCM

Transformation of Amalthea models to RCM models and back-propagation of timing analysis results.

In its current implementation the program is run from the command line with the required arguments detailed below.

## Arguments

The following arguments are needed to run the program.
Note that either -transform or -backannotate must be set, but not both at the same time.

#### -amalthea (-a)
Specify the path to the Amalthea model.

#### -rcm (-r)
Specify the path to the destination folder of the RCM project.

#### -transform (-t)
Flag to indicate that the Amalthea model shall be transformed to an RCM model.

#### -backannotate (-b)
Flag to indicate that the timing analysis results of the RCM model shall be back-annotated to the Amalthea model.
