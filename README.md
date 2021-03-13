# restaurant-puzzle

This project is meant to solve a little word scramble puzzle. The puzzle gives a list of words, which should be reordered such that they sound like some other coherent collection of words. For example, "TAB ESS MEANT LISH" might be rearranged to sound like "ESTABLISHMENT." Due to the strict adherence to the phoneme dictionary, however..the program won't currently make that link.

## Usage

Currently just looks for one line of clue words in `resources/clue_words.txt`, the phoneme dictionary in `resources/cmudict-0.7b`, and spits out the answers into `resources/answers.txt`. 

## License

Copyright © 2021 theChad

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
