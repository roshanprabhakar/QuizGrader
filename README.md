QUIZ GRADER

Quiz Grader is a simple grading tool aimed to facilitate the
grading of assessments in a manner which effectively utilizes 
short-term memory.

The grader allows teachers to grade specific assessments, not 
student by student, but instead problem by problem. Specifically,
this grader replaces the conventional grading method of incrementally
grading each student's assessment with instead a system where all 
collective responses to each problem are graded, incrementing the 
problem number

by allowing teachers to look at two separate responses to the
same question, the teacher is able to preserve the context of the
problem, as well as generalizable thoughts regarding the problem, 
as he or she assess the rest of his or her students' understandings. 

<br>
IMPORTANT

- As of right now: On occasional executions, LayoutExceptions 
are thrown. These are unimportant, simply restart the grader and the
program picks up where you left off. (for developers, the problem
occurs upon initializing a ProgressPane)
- In providing names for the appropriate window, it is advised 
that only first names are provided, for the sake of ease in usability

<br>
FEATURES

- The program allows the user to save the current execution state. 
Progress is saved after every window submission (when you click submit on any window)
- A software readable format is provided, along with an email exportable, all found in the
generated data folder

<br>
USAGE

- Move the scanned test pdf into the PDFInput folder.
    - If a file already exsts within this folder, replace it and title
    the new folder QGTestData.pdf
- Run the program using intellij's virtual environment. Make sure the
proper libraries are installed
- provde a list of the names of the students, in the order they appear on
the test, comma separated when prompted
- provde the number of pages in the assessment when prompted
- At any time, the program may be halted without disturbance to the 
grading session
- For every window that appears, fill out the appropriate information
(The score field is the only required field) - ignore all runtime exceptions
regarding this phase of the grading session
- After all windows are submitted, Two summary windows are provided along with
the generated data, in order to provide you with a way of browsing the collected data
- Closing this window will close erase all progress saving data, but will not delete files in ./Data
- NOTE: the PDFInput is never removed by the software, for the sake of not accidentally deleting large amounts
of data. After every grading execution, manually replace the file in this location. 