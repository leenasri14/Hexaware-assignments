from pydantic import BaseModel

class UserBase(BaseModel):
    employee_id: str
    role: str

class UserCreate(UserBase):
    password: str

class User(UserBase):
    id: int

    class Config:
        from_attributes = True  # Use from_attributes instead of orm_mode


