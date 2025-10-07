import re

class MessageUtil():
    """
    Utility class for analyzing SMS messages, particularly to determine 
    if a message is related to a financial transaction (Bank SMS).
    """

    def isBankSms(self, message: str) -> bool:
        """
        Checks if a message contains keywords commonly found in bank or 
        transactional SMS, ignoring case.

        Args:
            message: The string content of the SMS.

        Returns:
            True if financial keywords are found, False otherwise.
        """
        # Enhanced list of keywords for robust bank SMS detection
        words_to_search = [
            'spent', 'card', 'bank', 'debited', 'credited', 
            'txn', 'transaction', 'transfer', 'payment', 'paid', 
            'Rs', 'INR', 'amount', 'balance', 'account', 'Acct', 
            'A/C', 'wallet', 'loan', 'EMI', 'UPI', 'secure'
        ] 
        
        # Create a case-insensitive, whole-word-match regular expression pattern
        # r'\b(?:word1|word2|word3)\b' ensures we match 'card' but not 'discard'

        pattern = r'\b(?:' + '|'.join(re.escape(word) for word in words_to_search) + r')\b'
        
        # Search the message for the pattern
        return bool(re.search(pattern, message, re.IGNORECASE))