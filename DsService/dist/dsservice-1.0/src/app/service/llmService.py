import os
from langchain_core.prompts import ChatPromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from dotenv import load_dotenv
from app.entity.Event import Expense 

class LLMService:
    def __init__(self):
        # Load environment variables
        load_dotenv()

        # Enhanced prompt for structured extraction
        self.prompt = ChatPromptTemplate.from_messages(
            [
                (
                    "system",
                    "You are a highly accurate information extraction assistant. "
                    "Extract structured expense information from the input text. "
                    "Follow these rules:\n"
                    "1. Extract these attributes: amount, vendor, category, payment_method, date, notes.\n"
                    "2. If an attribute is missing, return null.\n"
                    "3. Return output only in JSON format matching the schema.\n"
                    "4. Normalize the data:\n"
                    "   - Amount should be numeric (no currency symbols)\n"
                    "   - Date in YYYY-MM-DD format if present\n"
                    "   - Vendor and category as plain strings\n"
                    "5. If multiple values exist, choose the primary one mentioned first.\n"
                    "6. Ignore unrelated information."
                ),
                ("human", "{text}")
            ]
        )

        # Load API key
        google_api_key = os.getenv("GOOGLE_API_KEY")
        if not google_api_key:
            raise ValueError("GOOGLE_API_KEY environment variable not set.")

        # Initialize the LLM with Gemini 2.5
        self.llm = ChatGoogleGenerativeAI(
            model="gemini-2.5-flash-lite",
            temperature=0.7,
            google_api_key=google_api_key
        )

        # Combine prompt + LLM with structured output schema
        self.runnable = self.prompt | self.llm.with_structured_output(schema=Expense)

    def runLLM(self, message: str):   
        print("Invoking LLM with message:", message)
        try:
            ans = self.runnable.invoke({"text": message})
            print("Raw LLM response:", ans)
        except Exception as e:
            print("Error invoking LLM:", e)
            raise
        
        # Return structured dict if possible
        if hasattr(ans, "model_dump"):
            return ans.model_dump()
        return ans
