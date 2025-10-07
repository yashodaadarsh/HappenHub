from .llmService import LLMService
class EventService:
    def __init__(self):
        self.llmService = LLMService()

    def process_event(self,event):
        return self.llmService.runLLM(event)