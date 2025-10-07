import os
from datetime import datetime
from langchain_core.prompts import ChatPromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from dotenv import load_dotenv
from entity.Event import Event

class LLMService:
    def __init__(self):
        # Load environment variables
        load_dotenv()

        # Enhanced prompt for structured extraction
        self.prompt = ChatPromptTemplate.from_messages([
            (
                "system",
                "You are a highly accurate event extraction assistant. "
                "Extract structured event information from the input JSON. "
                "Follow these rules:\n"
                "1. Extract these attributes: event_id, title, image_url, event_link, location, salary, start_date, end_date, type, description.\n"
                "2. The input JSON contains a field 'current_date' representing the current timestamp (yyyy-MM-dd HH:mm:ss). "
                "Use this 'current_date' to resolve all relative or partial dates.\n"
                "   - Relative dates (e.g., 'Just now', 'today', '1 week ago', '2 days ago') → convert to exact timestamp using 'current_date'.\n"
                "   - Partial dates (e.g., 'May 14') → complete with the year from 'current_date'.\n"
                "   - Missing or null start_date → assign start of the month of 'current_date'.\n"
                "   - Missing or null end_date → assign end of the month of 'current_date'.\n"
                "3. **If the salary is in a foreign currency, convert it to Indian Rupees (INR) using a reasonable exchange rate from your training data. Prepend the Indian Rupee symbol ('\u20b9') to the final numerical value.**\n"
                "4. Simplify description to max 100 words while keeping key responsibilities and skills.\n"
                "5. Return strictly valid JSON matching the schema, no extra text.\n"
                "6. Ensure all date strings are valid java.util.Date format (yyyy-MM-dd HH:mm:ss)."
            ),
            ("human", "Current Date: {current_date}\n\nEvent Details: {text}")
        ])

        # Load API key
        google_api_key = os.getenv("GOOGLE_API_KEY")
        if not google_api_key:
            raise ValueError("GOOGLE_API_KEY environment variable not set.")

        # Initialize the LLM with Gemini 2.5
        self.llm = ChatGoogleGenerativeAI(
            model="gemini-2.5-flash",
            temperature=0.7,
            google_api_key=google_api_key
        )

        # Combine prompt + LLM with structured output schema
        self.runnable = self.prompt | self.llm.with_structured_output(schema=Event)

    def runLLM(self, message: str):
        current_date = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        payload = {
            "text": message,
            "current_date": current_date
        }

        print("Invoking LLM with message:", payload)
        try:
            ans = self.runnable.invoke(payload)



            print("Raw LLM response:", ans)
        except Exception as e:
            print("Error invoking LLM:", e)
            raise

        if hasattr(ans, "model_dump"):
            return ans.model_dump()
        return ans