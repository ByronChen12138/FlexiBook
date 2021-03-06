/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.model;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 1 "../../../../../AppointmentStateMachine.ump"
// line 99 "../../../../../FlexiBookPersistence.ump"
// line 89 "../../../../../FlexiBook.ump"
public class Appointment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment State Machines
  public enum AppointmentStatus { BeforeDate, Final, InProgress }
  private AppointmentStatus appointmentStatus;

  //Appointment Associations
  private Customer customer;
  private BookableService bookableService;
  private List<ComboItem> chosenItems;
  private TimeSlot timeSlot;
  private FlexiBook flexiBook;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(Customer aCustomer, BookableService aBookableService, TimeSlot aTimeSlot, FlexiBook aFlexiBook)
  {
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create appointment due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddBookableService = setBookableService(aBookableService);
    if (!didAddBookableService)
    {
      throw new RuntimeException("Unable to create appointment due to bookableService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    chosenItems = new ArrayList<ComboItem>();
    if (!setTimeSlot(aTimeSlot))
    {
      throw new RuntimeException("Unable to create Appointment due to aTimeSlot. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddFlexiBook = setFlexiBook(aFlexiBook);
    if (!didAddFlexiBook)
    {
      throw new RuntimeException("Unable to create appointment due to flexiBook. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setAppointmentStatus(AppointmentStatus.BeforeDate);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getAppointmentStatusFullName()
  {
    String answer = appointmentStatus.toString();
    return answer;
  }

  public AppointmentStatus getAppointmentStatus()
  {
    return appointmentStatus;
  }

  public boolean updateDateAndTime(Date sysDate,Time sysTime,Date newStartDate,Time newStartTime,Date newEndDate,Time newEndTime)
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case BeforeDate:
        if (!(isOnDate(sysDate)))
        {
          // line 9 "../../../../../AppointmentStateMachine.ump"
          doUpdateDateAndTime(newStartDate, newStartTime, newEndDate, newEndTime);
          setAppointmentStatus(AppointmentStatus.BeforeDate);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelAppointment(Date sysDate)
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case BeforeDate:
        if (!(isOnDate(sysDate)))
        {
          // line 14 "../../../../../AppointmentStateMachine.ump"
          doCancelAppointment();
          setAppointmentStatus(AppointmentStatus.Final);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean updateServiceOrServiceCombo(Date sysDate,TimeSlot newTimeSlot,BookableService newBookableService,List<ComboItem> newChosenItems)
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case BeforeDate:
        if (!(isOnDate(sysDate)))
        {
          // line 19 "../../../../../AppointmentStateMachine.ump"
          doUpdateAppointment(newTimeSlot, newBookableService, newChosenItems);
          setAppointmentStatus(AppointmentStatus.BeforeDate);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean registerNoShow(Date sysDate,Time sysTime)
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case BeforeDate:
        if (isAfterAppointmentTime(sysDate,sysTime))
        {
          // line 24 "../../../../../AppointmentStateMachine.ump"
          doRegisterNoShow();
          this.delete();
          setAppointmentStatus(AppointmentStatus.Final);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startAppointment()
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case BeforeDate:
        setAppointmentStatus(AppointmentStatus.InProgress);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addOptionalServices(ComboItem newOptionalService)
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case InProgress:
        if (canAddOptionalService(newOptionalService))
        {
          // line 37 "../../../../../AppointmentStateMachine.ump"
          this.addChosenItem(newOptionalService);
          setAppointmentStatus(AppointmentStatus.InProgress);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean endAppointment()
  {
    boolean wasEventProcessed = false;

    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case InProgress:
        // line 41 "../../../../../AppointmentStateMachine.ump"
        this.delete();
        setAppointmentStatus(AppointmentStatus.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setAppointmentStatus(AppointmentStatus aAppointmentStatus)
  {
    appointmentStatus = aAppointmentStatus;

    // entry actions and do activities
    switch(appointmentStatus)
    {
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public BookableService getBookableService()
  {
    return bookableService;
  }
  /* Code from template association_GetMany */
  public ComboItem getChosenItem(int index)
  {
    ComboItem aChosenItem = chosenItems.get(index);
    return aChosenItem;
  }

  public List<ComboItem> getChosenItems()
  {
    List<ComboItem> newChosenItems = Collections.unmodifiableList(chosenItems);
    return newChosenItems;
  }

  public int numberOfChosenItems()
  {
    int number = chosenItems.size();
    return number;
  }

  public boolean hasChosenItems()
  {
    boolean has = chosenItems.size() > 0;
    return has;
  }

  public int indexOfChosenItem(ComboItem aChosenItem)
  {
    int index = chosenItems.indexOf(aChosenItem);
    return index;
  }
  /* Code from template association_GetOne */
  public TimeSlot getTimeSlot()
  {
    return timeSlot;
  }
  /* Code from template association_GetOne */
  public FlexiBook getFlexiBook()
  {
    return flexiBook;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeAppointment(this);
    }
    customer.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBookableService(BookableService aBookableService)
  {
    boolean wasSet = false;
    if (aBookableService == null)
    {
      return wasSet;
    }

    BookableService existingBookableService = bookableService;
    bookableService = aBookableService;
    if (existingBookableService != null && !existingBookableService.equals(aBookableService))
    {
      existingBookableService.removeAppointment(this);
    }
    bookableService.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfChosenItems()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addChosenItem(ComboItem aChosenItem)
  {
    boolean wasAdded = false;
    if (chosenItems.contains(aChosenItem)) { return false; }
    chosenItems.add(aChosenItem);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeChosenItem(ComboItem aChosenItem)
  {
    boolean wasRemoved = false;
    if (chosenItems.contains(aChosenItem))
    {
      chosenItems.remove(aChosenItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addChosenItemAt(ComboItem aChosenItem, int index)
  {
    boolean wasAdded = false;
    if(addChosenItem(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveChosenItemAt(ComboItem aChosenItem, int index)
  {
    boolean wasAdded = false;
    if(chosenItems.contains(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    }
    else
    {
      wasAdded = addChosenItemAt(aChosenItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTimeSlot(TimeSlot aNewTimeSlot)
  {
    boolean wasSet = false;
    if (aNewTimeSlot != null)
    {
      timeSlot = aNewTimeSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFlexiBook(FlexiBook aFlexiBook)
  {
    boolean wasSet = false;
    if (aFlexiBook == null)
    {
      return wasSet;
    }

    FlexiBook existingFlexiBook = flexiBook;
    flexiBook = aFlexiBook;
    if (existingFlexiBook != null && !existingFlexiBook.equals(aFlexiBook))
    {
      existingFlexiBook.removeAppointment(this);
    }
    flexiBook.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeAppointment(this);
    }
    BookableService placeholderBookableService = bookableService;
    this.bookableService = null;
    if(placeholderBookableService != null)
    {
      placeholderBookableService.removeAppointment(this);
    }
    chosenItems.clear();
    timeSlot = null;
    FlexiBook placeholderFlexiBook = flexiBook;
    this.flexiBook = null;
    if(placeholderFlexiBook != null)
    {
      placeholderFlexiBook.removeAppointment(this);
    }
  }

  // line 47 "../../../../../AppointmentStateMachine.ump"
  private boolean canAddOptionalService(ComboItem newOptionalService){
    boolean isTheSameServiceCombo = newOptionalService.getServiceCombo().equals(this.bookableService);
    return isTheSameServiceCombo;
  }

  // line 52 "../../../../../AppointmentStateMachine.ump"
  private boolean isOnDate(Date sysDate){
    return this.timeSlot.getStartDate().toString().equals(sysDate.toString());
  }

  // line 56 "../../../../../AppointmentStateMachine.ump"
  private boolean isAfterAppointmentTime(Date sysDate, Time sysTime){
    Date appointmentStartDate = this.timeSlot.getStartDate();
    Time appointmentStartTime = this.timeSlot.getStartTime();

    if(sysDate.before(appointmentStartDate))
    {return false;}


    else {
      boolean a=sysDate.toString().compareTo(appointmentStartDate.toString())>0;
      boolean b=sysTime.toString().equals(appointmentStartTime.toString());
      boolean c=sysTime.toString().compareTo(appointmentStartTime.toString())>0;
      boolean d=a||b||c;
      return d;}
  }

  // line 73 "../../../../../AppointmentStateMachine.ump"
  private void doUpdateDateAndTime(Date newStartDate, Time newStartTime, Date newEndDate, Time newEndTime){
    TimeSlot ts = this.getTimeSlot();
    ts.setStartDate(newStartDate);
    ts.setEndDate(newEndDate);
    ts.setStartTime(newStartTime);
    ts.setEndTime(newEndTime);
  }

  // line 81 "../../../../../AppointmentStateMachine.ump"
  private void doCancelAppointment(){
    this.delete();
  }

  // line 85 "../../../../../AppointmentStateMachine.ump"
  private void doUpdateAppointment(TimeSlot newTimeSlot, BookableService newBookableService, List<ComboItem> newChosenItems){
    this.setTimeSlot(newTimeSlot);
    this.setBookableService(newBookableService);
    this.chosenItems = new ArrayList<ComboItem>();
    if(newChosenItems != null) for(ComboItem item : newChosenItems) this.addChosenItem(item);
  }

  // line 92 "../../../../../AppointmentStateMachine.ump"
  private void doRegisterNoShow(){
    this.customer.setGugutime(customer.getGugutime() + 1);
  }

  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------

  // line 103 "../../../../../FlexiBookPersistence.ump"
  private static final long serialVersionUID = 11L ;


}