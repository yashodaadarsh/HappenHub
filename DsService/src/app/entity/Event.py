from pydantic import BaseModel, Field
from typing import Optional

class Event(BaseModel):
  
    # New event-related fields
    event_id: Optional[str] = Field(
        default=None,
        title="Event ID",
        description="Unique identifier for the event"
    )
    title: Optional[str] = Field(
        default=None,
        title="Event Title",
        description="Title of the event"
    )
    image_url: Optional[str] = Field(
        default=None,
        title="Image URL",
        description="URL of the event image"
    )
    event_link: Optional[str] = Field(
        default=None,
        title="Event Link",
        description="URL to the event page"
    )
    location: Optional[str] = Field(
        default=None,
        title="Event Location",
        description="Location where the event is held"
    )
    salary: Optional[str] = Field(
        default=None,
        title="Salary/Reward",
        description="Compensation or reward for the event"
    )
    start_date: Optional[str] = Field(
        default=None,
        title="Start Date",
        description="Start date of the event"
    )
    end_date: Optional[str] = Field(
        default=None,
        title="End Date",
        description="End date of the event"
    )
    type: Optional[str] = Field(
        default=None,
        title="Event Type",
        description="Type of the event"
    )
    description: Optional[str] = Field(
        default=None,
        title="Event Description",
        description="Description of the event"
    )
