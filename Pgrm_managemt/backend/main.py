# main.py
from fastapi import FastAPI, Depends, HTTPException, Request
from sqlalchemy.orm import Session
from database import SessionLocal
import crud
from auth import create_access_token, get_current_user
from datetime import timedelta
from pydantic import BaseModel

app = FastAPI()

# Dependency to get DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Request model for login
class LoginRequest(BaseModel):
    employee_id: str
    password: str

# Request model for chatbot
class PromptRequest(BaseModel):
    prompt: str

@app.post("/login")
def login(request: LoginRequest, db: Session = Depends(get_db)):
    user = crud.authenticate_user(db, request.employee_id, request.password)
    if not user:
        raise HTTPException(status_code=401, detail="Invalid employee ID or password")
    
    access_token_expires = timedelta(minutes=60)
    token = create_access_token(data={"sub": str(user.employee_id)}, expires_delta=access_token_expires)
    return {"access_token": token, "token_type": "bearer"}

# Dummy LLM call (replace with real one)
def call_llm(prompt: str) -> str:
    return f"LLM response to: {prompt}"

@app.post("/chat")
def chat(prompt_req: PromptRequest, request: Request, db: Session = Depends(get_db)):
    user = get_current_user(request, db)
    response = call_llm(prompt_req.prompt)
    return {
        "employee_id": user.employee_id,
        "response": response
    }
