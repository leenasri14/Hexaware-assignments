from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Boolean, Text, JSON
from sqlalchemy.orm import relationship, declarative_base,sessionmaker
from sqlalchemy import create_engine
from datetime import datetime

Base = declarative_base()

class Employee(Base):
    __tablename__ = 'employees'
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    email = Column(String(100), unique=True)
    department_id = Column(Integer, ForeignKey('departments.id'))
    skills = relationship("EmployeeSkill", back_populates="employee")

class AuthUser(Base):
    __tablename__ = 'auth_users'
    id = Column(Integer, primary_key=True)
    employee_id = Column(Integer, ForeignKey('employees.id'), unique=True)
    username = Column(String(50), unique=True, nullable=False)
    hashed_password = Column(String(255), nullable=False)
    role = Column(String(50), nullable=False)  # e.g., 'admin', 'manager', 'employee'
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    employee = relationship("Employee", backref="auth_user")


class Department(Base):
    __tablename__ = 'departments'
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    employees = relationship("Employee", backref="department")

class Project(Base):
    __tablename__ = 'projects'
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    client_id = Column(Integer, ForeignKey('clients.id'))
    assignments = relationship("ProjectAssignment", back_populates="project")

class Client(Base):
    __tablename__ = 'clients'
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)

class ProjectAssignment(Base):
    __tablename__ = 'project_assignments'
    id = Column(Integer, primary_key=True)
    project_id = Column(Integer, ForeignKey('projects.id'))
    employee_id = Column(Integer, ForeignKey('employees.id'))
    role = Column(String(50))
    project = relationship("Project", back_populates="assignments")
    employee = relationship("Employee")

class Task(Base):
    __tablename__ = 'tasks'
    id = Column(Integer, primary_key=True)
    title = Column(String(100))
    project_id = Column(Integer, ForeignKey('projects.id'))
    comments = relationship("TaskComment", back_populates="task")

class TaskComment(Base):
    __tablename__ = 'task_comments'
    id = Column(Integer, primary_key=True)
    task_id = Column(Integer, ForeignKey('tasks.id'))
    content = Column(Text)
    task = relationship("Task", back_populates="comments")

class TaskAttachment(Base):
    __tablename__ = 'task_attachments'
    id = Column(Integer, primary_key=True)
    task_id = Column(Integer, ForeignKey('tasks.id'))
    file_path = Column(String(255))

class Meeting(Base):
    __tablename__ = 'meetings'
    id = Column(Integer, primary_key=True)
    title = Column(String(100))
    scheduled_time = Column(DateTime)
    participants = relationship("MeetingParticipant", back_populates="meeting")

class MeetingParticipant(Base):
    __tablename__ = 'meeting_participants'
    id = Column(Integer, primary_key=True)
    meeting_id = Column(Integer, ForeignKey('meetings.id'))
    employee_id = Column(Integer, ForeignKey('employees.id'))
    meeting = relationship("Meeting", back_populates="participants")

class Skill(Base):
    __tablename__ = 'skills'
    id = Column(Integer, primary_key=True)
    name = Column(String(50))

class EmployeeSkill(Base):
    __tablename__ = 'employee_skills'
    id = Column(Integer, primary_key=True)
    employee_id = Column(Integer, ForeignKey('employees.id'))
    skill_id = Column(Integer, ForeignKey('skills.id'))
    level = Column(String(20))
    employee = relationship("Employee", back_populates="skills")

class Milestone(Base):
    __tablename__ = 'milestones'
    id = Column(Integer, primary_key=True)
    project_id = Column(Integer, ForeignKey('projects.id'))
    name = Column(String(100))
    due_date = Column(DateTime)

class Timesheet(Base):
    __tablename__ = 'timesheets'
    id = Column(Integer, primary_key=True)
    employee_id = Column(Integer, ForeignKey('employees.id'))
    hours = Column(Integer)
    date = Column(DateTime)

class Leave(Base):
    __tablename__ = 'leaves'
    id = Column(Integer, primary_key=True)
    employee_id = Column(Integer, ForeignKey('employees.id'))
    start_date = Column(DateTime)
    end_date = Column(DateTime)
    status = Column(String(20))

class Issue(Base):
    __tablename__ = 'issues'
    id = Column(Integer, primary_key=True)
    project_id = Column(Integer, ForeignKey('projects.id'))
    title = Column(String(100))
    description = Column(Text)
    status = Column(String(20))

class PerformanceReview(Base):
    __tablename__ = 'performance_reviews'
    id = Column(Integer, primary_key=True)
    employee_id = Column(Integer, ForeignKey('employees.id'))
    reviewer = Column(String(100))
    score = Column(Integer)
    feedback = Column(Text)

class Document(Base):
    __tablename__ = 'documents'
    id = Column(Integer, primary_key=True)
    name = Column(String(100))
    file_path = Column(String(255))
    uploaded_by = Column(Integer, ForeignKey('employees.id'))

class AuditLog(Base):
    __tablename__ = 'audit_logs'
    id = Column(Integer, primary_key=True)
    action = Column(String(255))
    user_id = Column(Integer, ForeignKey('employees.id'))
    timestamp = Column(DateTime, default=datetime.utcnow)

class HardwareAsset(Base):
    __tablename__ = 'hardware_assets'
    id = Column(Integer, primary_key=True)
    asset_tag = Column(String(50), unique=True)
    type = Column(String(50))
    assigned_to = Column(Integer, ForeignKey('employees.id'))

class ProjectHardwareUsage(Base):
    __tablename__ = 'project_hardware_usage'
    id = Column(Integer, primary_key=True)
    hardware_id = Column(Integer, ForeignKey('hardware_assets.id'))
    project_id = Column(Integer, ForeignKey('projects.id'))
    start_date = Column(DateTime)
    end_date = Column(DateTime)

class GitHubRepo(Base):
    __tablename__ = 'github_repos'
    id = Column(Integer, primary_key=True)
    name = Column(String(100))
    url = Column(String(255))
    project_id = Column(Integer, ForeignKey('projects.id'))

class RepoAccessLog(Base):
    __tablename__ = 'repo_access_logs'
    id = Column(Integer, primary_key=True)
    repo_id = Column(Integer, ForeignKey('github_repos.id'))
    user_id = Column(Integer, ForeignKey('employees.id'))
    accessed_at = Column(DateTime, default=datetime.utcnow)


# Additional tables would include:
# - Meeting, MeetingAttendance, Notification, TimeLog, Skill, UserSkill, 
# - Department, Team, TeamMember, Client, ClientProject, AuditLog, 
# - LeaveRequest, WorkSchedule, etc.

# Initialize database
DATABASE_URL = "postgresql+psycopg2://postgres:projectmgnt@localhost/project_management"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

Base.metadata.create_all(engine)



