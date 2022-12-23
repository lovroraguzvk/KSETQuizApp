# KSETQuizApp

Specialized Java Quiz app with the possibility of entering a score and displaying them.
Made for a club I volunteer at.

## Instructions

Start the app with the QuizApp.jar file.

Insert the name of the quiz, and select the folder with the apropriate question slides and also select an already
created excel file (where the data of the scores will be saved), if the excel already contains scores it will instead load them.

The app is controlled using keystrokes displayed below.
| Keystroke  | Action |
| ------------- | ------------- |
| RIGHT KEY ARROW  | Switch to the next question |
| CTRL + S  | Toggle form for score entering |
| CTRL + D  | Toggle developer mode |
| CTRL + R  | Reset all scores and data |

Developer mode allows the user to remove specific scores by enabling it and then clicking the displayed buttons.

There are no special rules of the quiz, the questions are random, and they do not repeat if they were already displayed in the last 10 questions.
Because of this the minimum amount of questions is 10, but you should aim for around 25 if you can.

The `src/main/java/Scoreboard.java` file contains constants at the top of the class which must be configured if you don't use 25 slides, 
or your individual slide file extension is not ".PNG". The same must be done if you don't want to save scores to a file called `ExampleData`.
Some additional things, such as the slide picture width inside the application (if you're using a large display), can be configured.
