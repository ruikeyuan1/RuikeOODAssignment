from collections import defaultdict
from pathlib import Path
import autograder
import unittest
import numpy as np

"""
In this assignment, you will implement a word n-gram.
This is a earlier natural language processing (NLP) model that "learns" and "predicts" words from a corpus.
Corpus is just a fancy word for a (set of) file(s) that contain text which is used to train language models.
For the full explanation see the assignment.
"""

def parse_text_file(file):
    """
    This function should parse a text file and return a list of sentences,
    where a sentence exists of list of words and should start
    with a special word "\<s>" and end with "</s>".
    """
    # Initialize an empty list to store sentences
    sentence_list = []

    # Open the specified file in read mode
    with open(file, 'r') as f:
        # Iterate through each line in the file
        for line in f:
            # Remove leading and trailing whitespaces, and convert to lowercase
            line = line.strip().lower()

            # Skip empty lines
            if line:
                # Split the line into a list of words
                words = line.split()

                # Insert the special word "<s>" at the beginning of the list
                words.insert(0, "<s>")

                # Append the special word "</s>" at the end of the list
                words.append("</s>")

                # Handle punctuation at the end of the sentence
                letter_of_last_word = list(words[-2])
                if letter_of_last_word[-1] == ".":
                    letter_of_last_word.pop()
                last_word = ''.join(letter_of_last_word)
                words[-2] = last_word

                # Append the modified list of words (representing a sentence) to the sentence list
                sentence_list.append(words)

    # Return the list of sentences
    return sentence_list


def set_history(history, new_word, name):
    """
    This function should return a new history depending on the model that is used and the new_word.
    name could be three values: unigram, bigram, or trigram.
    This function should be the only place where the three models have different code.
    See grading scheme *dry code*.

    If history is None, initialize the history correctly depending on the model.

    This function is optional to use. However, highly recommended to keep your code dry.
    """
    # Use a match statement to handle different cases based on the model name
    match name:
        # If the model is unigram, return "<any>" regardless of the history
        case "unigram":
            return "<any>" if history is None else "<any>"
        # If the model is bigram, return "<s>" if history is None, otherwise return the new word
        case "bigram":
            return "<s>" if history is None else new_word
        # If the model is trigram, return "<s>,<s>" if history is None,
        # otherwise return the second part of the history followed by the new word
        case "trigram":
            return "<s>,<s>" if history is None else history.split(",")[1] + "," + new_word


def create_defaultdict(type):
    """
    This is an optional function that returns a function that returns a defaultdict with the specified default type.

    EXPLANATION (see grading):
    """
    def return_defaultdict():
        return defaultdict(type)

    return return_defaultdict

def make_ngram_model(sentences, name):
    """
    To make an n-gram model, we need to translate all the sentences into
    a prediction model that tells us the chance for a random word.

    Each model will be stored in a dictionary that looks as follows.
    The keys of this dictionary are the history, which are the previous word(s) in the sentence.
    The values are a dictionary on their own with as keys all the possible words and
    as values the probability of that word following the history (previous words).

    For more details, see the exercise description.

    Tip 1: Before making the dictionary with all probabilities, make a similar dictionary with the counts.
    Tip 2: To make the code cleaner, you can use a `defaultdict` instead of a `dict`.
           This adds the benefit that if a key does not exist the value will be a default value
           and not throw the error KeyError. To make a defaultdict of defaultdict with int values.
           You can use defaultdict(create_defaultdict(int)), where create_defaultdict is a function that
           returns a defaultdict. This defaultdict has as default the input of the function `create_defaultdict`.
    Tip 3: If you want to loop over the keys of a dict you can type `for key in dict:`,
           If you want to loop over the values add .values() thus `for value in dict.values:`
           and for both you can use `for key, value in dict.items():`
    """
    # Initialize an empty dictionary to store the n-gram model
    ngram_model = {}

    # Iterate through each sentence in the corpus
    for sentence in sentences:
        # Initialize the history based on the model type
        history = set_history(None, None, name)

        # Iterate through the words to create histories and update the model
        for word in sentence:
            current_word = word

            # Update the model with the current history and word
            if history not in ngram_model:
                ngram_model[history] = {}

            if word != "<s>":
                if current_word not in ngram_model[history]:
                    ngram_model[history][current_word] = 1
                else:
                    ngram_model[history][current_word] += 1

            # Update the history for the next iteration
            history = set_history(history, current_word, name)

    # Convert counts to probabilities
    for history, word_dict in ngram_model.items():
        total_count = sum(word_dict.values())
        ngram_model[history] = {word: count / total_count for word, count in word_dict.items()}

    # Return the resulting n-gram model
    return ngram_model


# Function to predict and generate sentences using the given n-gram model
def predict_sentence(model, name, n_sentences=10, max_sentence_len=10):
    # Create a random number generator with a fixed seed for reproducibility
    rng = np.random.default_rng(seed=42)
    
    # Initialize an empty list to store generated sentences
    sentences = []

    # Loop through the specified number of sentences
    for i in range(n_sentences):
        # Initialize an empty list to store the current sentence
        sentence = []
        
        # Initialize the history based on the model type
        history = set_history(None, None, name)

        # Continue adding words to the sentence until the maximum length is reached
        while len(sentence) < max_sentence_len:
            # Retrieve the word probability dictionary for the current history from the model
            word_dict = model[history]

            # If the word dictionary is empty, break out of the loop
            if not word_dict:
                break

            # Choose the next word based on the probabilities in the dictionary
            next_word = rng.choice(list(word_dict.keys()), p=list(word_dict.values()))

            # If the chosen word is the end-of-sentence token, break out of the loop
            if next_word == "</s>":
                break

            # Append the chosen word to the current sentence
            sentence.append(next_word)
            
            # Update the history for the next iteration
            history = set_history(history, next_word, name)

        # Append the completed sentence to the list of sentences
        sentences.append(sentence)

    # Write the generated sentences to a file
    with open(f"generated_{name}_sentences.txt", 'w') as f:
        for sentence in sentences:
            f.write(" ".join(sentence) + "." + "\n")
        # Print a success message along with the filename where the results are saved
        print("\033[1;33mSuccessfully executed, the result has been saved into generated_" + name + "_sentences.txt\033[0m")
    
    # Return the list of generated sentences
    return sentences

# def test():
#     #
#     # """
#     # DO NOT CHANGE THE CODE BELOW!
#     # THESE TEST ARE VERY BASIC TEST TO GIVE AN IDEA IF YOU ARE ON THE RIGHT TRACK!
#     # """
#     #
#     class TestUnigram(unittest.TestCase):
#         pass
#
#     path_to_file = Path.cwd()
#     path_to_file = path_to_file.glob('**/test.txt').__next__()
#
#     parse_text_file_tests = [
#         (path_to_file, [['<s>', 'this', 'is', 'a', 'test', 'sentence', '</s>']])
#     ]
#
#     make_model_tests = [
#         (([['<s>', 'this', 'is', 'a', 'sentence', '</s>']], "unigram"),
#          {'<any>': {'this': 0.2,
#                     'a': 0.2,
#                     'is': 0.2,
#                     'sentence': 0.2,
#                     '</s>': 0.2}}),
#     ]
#
#     autograder.create_tests(TestUnigram, parse_text_file, parse_text_file_tests)
#     autograder.create_tests(TestUnigram, make_ngram_model, make_model_tests)
#
#     """
#     Here the test suite are made, each suite has its own class
#     """
#     suite = unittest.TestLoader().loadTestsFromTestCase(TestUnigram)
#     unittest.TextTestRunner(verbosity=2).run(suite)

#This function checks whether the input is "yes" or "no" and do the uppercase to lowercase conversion when checking
def yes_or_no_convert(string_input):
    # Define lists of possible 'yes' and 'no' inputs
    list_of_possible_yes = ['y', 'yes']
    list_of_possible_no = ['n', 'no']

    match string_input.lower():
        case value if value in list_of_possible_yes:
            return "yes"
        case value if value in list_of_possible_no:
            return "no"
        case _:
            # If the input is not recognized as either 'yes' or 'no', return None
            return None

def generate_size_question():
    # Ask the user for the desired sentence size and provide instructions
    sentence_max_length = input(
        "\033[1;31mDefine the maximum length of the sentence you want the machine to generate (5 to 30)(The standard size is 8)?\033[0m ")
    number_of_sentences = input(
        "\033[1;31mFor how many sentences you want the machine to generate (10 to 30)(The standard size is 10)?\033[0m ")
   

    # Check if the user's input is numeric and within the valid range
    if sentence_max_length.isnumeric() and 5 <= int(
            sentence_max_length) <= 30 and number_of_sentences.isnumeric() and 10 <= int(number_of_sentences) <= 30:
            return int(sentence_max_length), int(number_of_sentences)

    # Print an appropriate message if the input is not valid
    print("\033[1;33mPlease enter a valid value.\033[0m")
    return -1

# Function to evaluate and get the type of n-gram model from user input
def evaluate_n_gram_input():
    # Prompt the user for the type of n-gram model they want to use
    type_of_grams = input("\033[1;31mWhich model you would like to use (bigram/trigram/unigram)?\033[0m ")

    # Check the user input and return the corresponding n-gram model type
    if type_of_grams.lower() == "bigram":
        return "bigram"
    elif type_of_grams.lower() == "trigram":
        return "trigram"
    elif type_of_grams.lower() == "unigram":
        return "unigram"
    else:
        # If the input doesn't match any valid type, return 0
        return 0


def main():   
    # Prompt the user for input to determine whether to run the sentence generator
    raw_input = input("\033[1;32mDo you want to run the sentence generator (yes/no): \033[0m")

    # Use a match statement to handle different cases of user input using the yes_or_no_convert function
    match yes_or_no_convert(raw_input):
        # If the user input is "yes" or an equivalent, execute the following block
        case "yes":
            # Use another match statement to handle the result of the generate_size_question function
            match generate_size_question():
                # If the result is not -1 (indicating valid input), execute the following block
                case size_question_result if size_question_result != -1:
                    # Extract values from size_question_result and convert them to integers
                    sentence_max_length, number_of_sentences = map(int, size_question_result)
                    
                    # Get the current working directory
                    path = Path.cwd() 
                    
                    # Locate the path of the 'abc_corpus.txt' file in the current directory
                    path = path.glob('abc_corpus.txt').__next__()
                    evaluate_n_gram_result = evaluate_n_gram_input() 
                    # check if the input for gram model is correct, if so run the function predict_sentence
                    if evaluate_n_gram_result != 0:
                        # Create n gram model based on the 'abc_corpus.txt' file
                        model = make_ngram_model(parse_text_file(path), evaluate_n_gram_result)
    
                        # Generate sentences using the trigram model
                        predict_sentence(model, evaluate_n_gram_result, n_sentences=number_of_sentences,
                                        max_sentence_len=sentence_max_length)
                    else:
                          print("\033[1;33mPlease select a right value.\033[0m")
                # If the result is -1, print a message indicating an incorrect value was selected
                case _:
                    print("\033[1;33mPlease select a right value.\033[0m")
        # If the user input is not "yes" or an equivalent, print a message indicating incorrect input
        case _:
            print("\033[1;33mPlease enter a correct value.\033[0m")

if __name__ == "__main__":
    #test()
    main()






