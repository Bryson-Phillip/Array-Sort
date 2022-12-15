# introduction

The purpose of this project is to repurpose the skills and concepts used in earlier
projects to design an array sorting algorithm. This algorithm should allow for any
number of threads operating on arrays of variable sizes and should abstract the
interaction of cores (threads) for an ever increasing number of threads. This program
will also implement a server-client interaction, with the client establishing a connection
with the server and sending the contents of the array, which are randomized. The server
will listen for the contents, sort them using threads that are spawned from its main
function and return the sorted array back to the client. The objective is to see how
aspects of speed and efficiency change as thread pools and array sizes change.

# Design Approach

The program is designed to use data parralellism, as opposed to task parallelism. Each
thread operates the same as any other thread but will process different, similarly
structured, sets of data from within the array. Each thread has a unique id that allows it
to be identified when assigning a portion of the array to work on. The algorithm chosen
to sort the array requires multiple iterations of sorting over the entire array, and thus
threads must, at times, wait for other threads operating within an earlier iteration to
finish. However, for threads on parallel portions of the array for the same level, or for
non overlapping portions on different levels, process interleaving will be exploited as
much as possible.

# Methodology

My program is an implementation of merge sort. Merge sort operates by dividing the
array into subsections that are half of the size of the parent until each section is only
two indices wide. From there the algorithm travels back up using the sort. In each
section there are two pointers. One point points to the beginning of the section, and the
section pointer points to the index after the half-way point. The code successively
checks which index points to the lower value and shifts the corresponding pointer to the
right until it reaches the end of its domain, This results in a new array that combines two
sections that had, individually, been sorted by previous iterations. Subsections of the
starting array may be processed in series or parallel, but in theory parallelizing should
result in speedups if done correctly. Once two arrays are done sorting adjacent
subsections, an idle thread can be assigned to sort both of them together on a higher
level. This process continues until one thread sorts the span of the starting array. Any
idle thread may be assigned to a currently operable section of code. So, the more
threads, the greater the portion of the array that can be sorted at once. This is
bottlenecked by the requirement that previous subsections must be sorted first. At some
point adding more threads will not be helpful as most of them will be idling.

# Implementation

The server and client classes operate independently. The server class creates a server
socket which listens for the clients connection. The first two sends from the client
contain the size of the array and the number of threads. A for-loop then iterates on both
sides to send each array index from the client side to the server side. For each pThread
created, the run() function calls “set()” which looks at the last assigned thread and
assigns the caller to the next available spot. This is done synchronously to avoid race
conditions where more than one thread is assigned to overlapping spots. At each level,
spots are assigned from right to left across the array. The range of values to be sorted
by a given thread is calculated as the (start-1) + 2^(level) where level is increased after
threads reach the end of the array. (start-1) marks where the range of the previously
assigned thread ends. The span increases as 2, 4, 8, 16…
Sorting is done by creating another temporary array ‘temp’ to store the values selected
by each pointer in the subarray. Once the data is sorted, the values of the temp
overwrite the original array values at the appropriate start and end points.
