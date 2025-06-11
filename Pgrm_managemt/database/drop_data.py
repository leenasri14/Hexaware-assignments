from sqlalchemy.orm import sessionmaker
from database_design import engine, RepoAccessLog, GitHubRepo, ProjectHardwareUsage, HardwareAsset, \
    AuditLog, Document, PerformanceReview, Issue, Leave, Timesheet, Milestone, \
    EmployeeSkill, Skill, MeetingParticipant, Meeting, TaskAttachment, TaskComment, Task, \
    ProjectAssignment, Project, Client, AuthUser, Employee, Department

SessionLocal = sessionmaker(bind=engine)
db = SessionLocal()

def clear_all_data():
    db.query(RepoAccessLog).delete()
    db.query(GitHubRepo).delete()
    db.query(ProjectHardwareUsage).delete()
    db.query(HardwareAsset).delete()
    db.query(AuditLog).delete()
    db.query(Document).delete()
    db.query(PerformanceReview).delete()
    db.query(Issue).delete()
    db.query(Leave).delete()
    db.query(Timesheet).delete()
    db.query(Milestone).delete()
    db.query(EmployeeSkill).delete()
    db.query(Skill).delete()
    db.query(MeetingParticipant).delete()
    db.query(Meeting).delete()
    db.query(TaskAttachment).delete()
    db.query(TaskComment).delete()
    db.query(Task).delete()
    db.query(ProjectAssignment).delete()
    db.query(Project).delete()
    db.query(Client).delete()
    db.query(AuthUser).delete()
    db.query(Employee).delete()
    db.query(Department).delete()

    db.commit()
    print("All data deleted from tables.")

clear_all_data()
