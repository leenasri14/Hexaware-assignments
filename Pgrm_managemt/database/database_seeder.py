# database_seeder.py
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from database_design import Base, Employee, Department, Project, Client, ProjectAssignment, Task, TaskComment, \
    TaskAttachment, Meeting, MeetingParticipant, Skill, EmployeeSkill, Milestone, Timesheet, Leave, Issue, \
    PerformanceReview, Document, AuditLog, HardwareAsset, ProjectHardwareUsage, GitHubRepo, RepoAccessLog, AuthUser
from faker import Faker
import random
from datetime import datetime, timedelta

# Initialize Faker
fake = Faker()

# Database connection (same as in database_design.py)
DATABASE_URL = "postgresql+psycopg2://postgres:projectmgnt@localhost/project_management"
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
db = SessionLocal()

def seed_database():
    # Clear existing data
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

    # Seed Departments
    departments = []
    for _ in range(20):
        dept = Department(
            name=fake.company_suffix() + " Department"
        )
        departments.append(dept)
        db.add(dept)
    db.commit()

    # Seed Skills
    skills = []
    skill_names = ["Python", "JavaScript", "Project Management", "SQL", "React", 
                   "Vue.js", "Docker", "AWS", "Azure", "UI/UX Design", 
                   "Data Analysis", "Machine Learning", "DevOps", "Java", "C#",
                   "Ruby on Rails", "PHP", "Swift", "Kotlin", "Go"]
    for name in skill_names:
        skill = Skill(name=name)
        skills.append(skill)
        db.add(skill)
    db.commit()

    # Seed Employees and AuthUsers
    employees = []
    for i in range(20):
        emp = Employee(
            name=fake.name(),
            email=fake.email(),
            department_id=random.choice(departments).id
        )
        employees.append(emp)
        db.add(emp)
        
        # Create auth user for each employee
        auth_user = AuthUser(
            employee_id=emp.id,
            username=fake.user_name(),
            hashed_password=fake.sha256(),  # In real app, use proper password hashing
            role=random.choice(['admin', 'manager', 'employee']),
            is_active=random.choice([True, False])
        )
        db.add(auth_user)
    db.commit()

    # Seed EmployeeSkills
    for emp in employees:
        for _ in range(random.randint(1, 5)):
            emp_skill = EmployeeSkill(
                employee_id=emp.id,
                skill_id=random.choice(skills).id,
                level=random.choice(['Beginner', 'Intermediate', 'Advanced', 'Expert'])
            )
            db.add(emp_skill)
    db.commit()

    # Seed Clients
    clients = []
    for _ in range(20):
        client = Client(
            name=fake.company()
        )
        clients.append(client)
        db.add(client)
    db.commit()

    # Seed Projects
    projects = []
    for _ in range(20):
        project = Project(
            name=fake.catch_phrase(),
            client_id=random.choice(clients).id
        )
        projects.append(project)
        db.add(project)
    db.commit()

    # Seed Project Assignments
    for proj in projects:
        for _ in range(random.randint(1, 5)):
            assignment = ProjectAssignment(
                project_id=proj.id,
                employee_id=random.choice(employees).id,
                role=random.choice(['Developer', 'Manager', 'Designer', 'QA', 'Analyst'])
            )
            db.add(assignment)
    db.commit()

    # Seed Tasks
    tasks = []
    for proj in projects:
        for _ in range(random.randint(3, 8)):
            task = Task(
                title=fake.sentence(nb_words=4),
                project_id=proj.id
            )
            tasks.append(task)
            db.add(task)
    db.commit()

    # Seed Task Comments
    for task in tasks:
        for _ in range(random.randint(0, 5)):
            comment = TaskComment(
                task_id=task.id,
                content=fake.paragraph(nb_sentences=3),
            )
            db.add(comment)
    db.commit()

    # Seed Task Attachments (fewer than other entities)
    for task in random.sample(tasks, 20):
        attachment = TaskAttachment(
            task_id=task.id,
            file_path=fake.file_path(depth=3)
        )
        db.add(attachment)
    db.commit()

    # Seed Meetings
    meetings = []
    for _ in range(20):
        meeting = Meeting(
            title=fake.sentence(nb_words=3),
            scheduled_time=fake.date_time_this_year()
        )
        meetings.append(meeting)
        db.add(meeting)
    db.commit()

    # Seed Meeting Participants
    for meeting in meetings:
        for _ in range(random.randint(3, 8)):
            participant = MeetingParticipant(
                meeting_id=meeting.id,
                employee_id=random.choice(employees).id
            )
            db.add(participant)
    db.commit()

    # Seed Milestones
    for proj in projects:
        for i in range(random.randint(1, 3)):
            milestone = Milestone(
                project_id=proj.id,
                name=f"Milestone {i+1}",
                due_date=fake.date_time_between(start_date='-30d', end_date='+90d')
            )
            db.add(milestone)
    db.commit()

    # Seed Timesheets
    for emp in employees:
        for _ in range(random.randint(5, 15)):
            timesheet = Timesheet(
                employee_id=emp.id,
                hours=random.randint(1, 12),
                date=fake.date_this_year()
            )
            db.add(timesheet)
    db.commit()

    # Seed Leave
    for emp in employees:
        if random.choice([True, False]):  # Only some employees take leave
            leave = Leave(
                employee_id=emp.id,
                start_date=fake.date_this_year(),
                end_date=fake.date_this_year(),
                status=random.choice(['Pending', 'Approved', 'Rejected'])
            )
            db.add(leave)
    db.commit()

    # Seed Issues
    for proj in projects:
        for _ in range(random.randint(1, 5)):
            issue = Issue(
                project_id=proj.id,
                title=fake.sentence(nb_words=5),
                description=fake.paragraph(nb_sentences=3),
                status=random.choice(['Open', 'In Progress', 'Resolved', 'Closed'])
            )
            db.add(issue)
    db.commit()

    # Seed Performance Reviews
    for emp in employees:
        review = PerformanceReview(
            employee_id=emp.id,
            reviewer=random.choice(employees).name,
            score=random.randint(1, 5),
            feedback=fake.paragraph(nb_sentences=4)
        )
        db.add(review)
    db.commit()

    # Seed Documents
    for emp in employees:
        for _ in range(random.randint(1, 3)):
            doc = Document(
                name=fake.file_name(),
                file_path=fake.file_path(depth=2),
                uploaded_by=emp.id
            )
            db.add(doc)
    db.commit()

    # Seed Audit Logs
    for _ in range(50):  # More audit logs than other entities
        log = AuditLog(
            action=fake.sentence(nb_words=6),
            user_id=random.choice(employees).id,
            timestamp=fake.date_time_this_year()
        )
        db.add(log)
    db.commit()

    # Seed Hardware Assets
    hardware = []
    for _ in range(20):
        asset = HardwareAsset(
            asset_tag=fake.bothify(text='AST-#####'),
            type=random.choice(['Laptop', 'Desktop', 'Tablet', 'Phone', 'Monitor']),
            assigned_to=random.choice(employees).id if random.choice([True, False]) else None
        )
        hardware.append(asset)
        db.add(asset)
    db.commit()

    # Seed Project Hardware Usage
    for asset in hardware:
        if random.choice([True, False]):  # Only some hardware is assigned to projects
            usage = ProjectHardwareUsage(
                hardware_id=asset.id,
                project_id=random.choice(projects).id,
                start_date=fake.date_this_year(),
                end_date=fake.date_this_year() if random.choice([True, False]) else None
            )
            db.add(usage)
    db.commit()

    # Seed GitHub Repos
    repos = []
    for proj in projects:
        if random.choice([True, False]):  # Not all projects have repos
            repo = GitHubRepo(
                name=f"{proj.name.lower().replace(' ', '-')}-repo",
                url=f"https://github.com/company/{proj.name.lower().replace(' ', '-')}",
                project_id=proj.id
            )
            repos.append(repo)
            db.add(repo)
    db.commit()

    # Seed Repo Access Logs
    for repo in repos:
        for _ in range(random.randint(5, 20)):
            access = RepoAccessLog(
                repo_id=repo.id,
                user_id=random.choice(employees).id,
                accessed_at=fake.date_time_this_year()
            )
            db.add(access)
    db.commit()

    print("Database seeded successfully!")

if __name__ == "__main__":
    seed_database()