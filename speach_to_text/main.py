import os
from fastapi import FastAPI, HTTPException, UploadFile, File, Form
from pydantic import BaseModel
from ModelsOfTranscribe import transribeAudio
from dotenv import load_dotenv
import uvicorn
import shutil
from logger_config import logger

# Load environment variables
load_dotenv()
app = FastAPI(title="Speech to Text API")

# TranscriptionRequest Pydantic model is no longer needed for the new /transcribe endpoint
@app.post("/transcribe")
async def transcribe_audio(
    model_name: str = Form("base"),
    file: UploadFile = File(...)
):
    audio_dir = "audio"
    os.makedirs(audio_dir, exist_ok=True)
    
    # Define file path
    file_path = os.path.join(audio_dir, file.filename)
    logger.info(f"Received transcription request for file: {file.filename} using model: {model_name}")
    
    try:
        # Save the uploaded file
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
        
        # get the transcibed audio
        transcript_text = transribeAudio(model_name,file_path) 
        # retunring the result to the client
        logger.info(f"Transcription successful for {file.filename} and the transcibed text is {transcript_text}")
        return {
            "status": "success",
            "model": model_name,
            "filename": file.filename,
            "transcript": transcript_text
        }
    except Exception as e:
        logger.error(f"Error during transcription of {file.filename}: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Transcription error: {str(e)}")
    finally:
        # Optional: You might want to delete the file after transcription if not needed
        # os.remove(file_path)
        pass

@app.get("/")
async def root():
    return {"message": "Speech to Text API is running. Use POST /transcribe with model_name and file_name."}

if __name__ == "__main__":     
    logger.info("Starting FastAPI server on http://0.0.0.0:8000")
    uvicorn.run(app, host="0.0.0.0", port=8000)