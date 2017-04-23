NAME
    tc - Run text classifier.

SYNOPSIS
    cd [--C] [--V] [--M] [--P] [--K]

DESCRIPTION
    Run text classifier.

    Run either K Nearest Neighbour or Nearest Centroid Classifier on a dataset with 5 fold cross validation.

    The dataset Path is defined in Main.java line 12 & 13 and stopwords are defined in line 14. These path must be valid
    for TC to run.

    If the classifier is executed successfully, the program prints the results and returns 1. Otherwise the program
    returns 0 and exits. In case of runtime exception, the program prints the stacktrace and returns 0.

    Arguments:
        --C     Classifier value. Choose --knn for k nearest neighbour and --ncc for nearest-centroid classifier.
        --V     Vector creation method. Choose --binary for Binary Vector and --frequency for frequency vector.
        --M     Data modification. Choose --modify to remove all non-alphabets, --origin to for no chage.
        --P     Select distance metric. --p=0 for Manhattan Distance Metric and --p=1 for Euclidean Distance Metric
        --K     Select number of nearest neighbour. Only valid for --knn classifier. Ignore in case of --ncc

    There is no default selection of strategy. All of the Arguments above must be specified unless stated otherwise in
    description.

    Exit Status:
    Returns 1 if classification is successful; zero otherwise.

