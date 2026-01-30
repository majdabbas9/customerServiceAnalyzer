import os
from fastapi import FastAPI, HTTPException
from dotenv import load_dotenv
from openai import OpenAI
from models import Emotion, AnalysisRequest, AnalysisResponse

load_dotenv()
app = FastAPI(title="Text Emotion Detector API")
client = OpenAI(
    api_key=os.getenv("API_KEY"), 
    base_url=os.getenv("BASE_URL")
)

def load_prompt():
    with open("prompt.txt", "r") as f:
        return f.read()

PROMPT_TEMPLATE = load_prompt()

@app.post("/analyze", response_model=AnalysisResponse)
async def analyze_emotion(request: AnalysisRequest):
    try:
        emotions_list = ", ".join([e.value for e in Emotion])
        # replce the {emotions} with the emotions list and {text} with the paramter from the client
        prompt = PROMPT_TEMPLATE.format(emotions=emotions_list, text=request.text)
        print(prompt)
        response = client.chat.completions.create(
            model=os.getenv("LLM_MODEL"), # or any other model you prefer
            messages=[
                {"role": "user", "content": prompt}
            ],
            temperature=0,
            max_tokens=10
        )
        
        detected_emotion = response.choices[0].message.content.strip().lower()
        
        # Validate that the returned emotion is in our Enum
        try:
            emotion = Emotion(detected_emotion)
        except ValueError:
            # Fallback or error handling if LLM returns something else
            # We can try to find if any of the enum values is in the response
            for e in Emotion:
                if e.value in detected_emotion:
                    emotion = e
                    break
            else:
                raise HTTPException(status_code=500, detail=f"LLM returned an invalid emotion: {detected_emotion}")
        
        return AnalysisResponse(text=request.text, emotion=emotion)
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)