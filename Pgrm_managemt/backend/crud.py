from sqlalchemy.orm import Session
from models import User
from passlib.context import CryptContext

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def get_user(db: Session, employee_id: str):
    return db.query(User).filter(User.employee_id == employee_id).first()

def authenticate_user(db: Session, employee_id: str, password: str):
    user = get_user(db, employee_id)
    if user and pwd_context.verify(password, user.password):  # Not hashed_password
        return user
    return None
