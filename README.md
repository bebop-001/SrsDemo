SrsDemo
=======

This is an implementation of the Super Memo Spaced SM-2 Spaced Repetition
algorithm with a UI that allows you to track the internal state.

References:
The Wikipedia page on Spaced Repetition
 * http://en.wikipedia.org/wiki/Spaced_repetition
From the supermemo site:
 * source for a supermemo plugin that implements the algorithm
 * ref:http://www.supermemo.com/english/ol/sm2source.htm
 * Explanation:
 * ref:http://www.supermemo.com/english/ol/sm2.htm
 * Algorithm with pencil/paper.
 * ref:http://www.supermemo.com/articles/paper.htm

The software implemnts:
 * a row of 5 "Assurance level" buttons that represent how sure
   you are of your answer: 1 being no knowledge and 5 being
   completely sure.
 * a TextView showing the state of the SRS Object's internal variables
 * a ListView that shows the state of the progressive changes to the
   state so you can see the results of pressing the different assurance
   levels.

Licensing:
According to this page on supermemo.com
http://www.supermemo.com/help/faq/algorithm.htm#40291-51
"...Our only requirement for such cases is a prominent credit given to the authors of SuperMemo. You have to include the following copyright note and site reference regarding the Algorithm SM-2:
Algorithm SM-2, © Copyright SuperMemo World, 1991.  www.supermemo.com, www.supermemo.eu"
