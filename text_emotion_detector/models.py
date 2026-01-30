from enum import Enum
from pydantic import BaseModel

class Emotion(str, Enum):
    SATISFIED = "satisfied"
    FRUSTRATED = "frustrated"
    ANGRY = "angry"
    CONFUSED = "confused"
    DISAPPOINTED = "disappointed"
    APPRECIATIVE = "appreciative"
    NEUTRAL = "neutral"

class AnalysisRequest(BaseModel):
    text: str

class AnalysisResponse(BaseModel):
    text: str
    emotion: Emotion
