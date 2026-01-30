from abc import ABC, abstractmethod
import whisper
from logger_config import logger

class SpeechModel(ABC):
    @abstractmethod
    def transcribe(self, file_path: str) -> str:
        """
        Transcribes the speech in the given file path to text.
        """
        pass

class WhisperModel(SpeechModel):
    def __init__(self, model_size: str = "base"):
        logger.info(f"Loading Whisper model: {model_size}")
        self.model = whisper.load_model(model_size)

    def transcribe(self, file_path: str) -> str:
        logger.info(f"Transcribing audio file: {file_path}")
        result = self.model.transcribe(file_path)
        return result["text"]

def transribeAudio(model_name: str,file_path: str) -> str :
    if model_name.lower() == "whisper":
        return WhisperModel("small").transcribe(file_path)