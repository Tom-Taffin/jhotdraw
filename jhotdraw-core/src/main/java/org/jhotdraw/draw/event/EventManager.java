package org.jhotdraw.draw.event;

import java.awt.geom.Rectangle2D;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;

/**
 * Manages events for a Figure (listeners and notifications).
 */
public class EventManager {

  private final Figure figure;
  private Drawing drawing;
  private final EventListenerList listenerList = new EventListenerList();

  /**
   * Creates an event manager for a figure.
   *
   * @param figure The figure that emits the events
   * @param drawing The drawing containing the figure (may be null)
   */
  public EventManager(Figure figure, Drawing drawing) {
    this.figure = figure;
    this.drawing = drawing;
  }

  public EventManager(Figure figure) {
    this(figure, null);
  }

  public void setDrawing(Drawing drawing) {
    this.drawing = drawing;
  }

  public void addFigureListener(FigureListener l) {
    if (Stream.of(listenerList.getListeners(FigureListener.class))
        .noneMatch(listener -> listener.equals(l))) {
      listenerList.add(FigureListener.class, l);
    }
  }

  public void removeFigureListener(FigureListener l) {
    listenerList.remove(FigureListener.class, l);
  }

  /**
   * Notifies an area invalidation to redraw.
   */
  public void fireAreaInvalidated(Rectangle2D.Double invalidatedArea) {
    fireFigureEvent(
        (listener, event) -> listener.areaInvalidated(event),
        () -> new FigureEvent(figure, invalidatedArea));
  }

  /**
   * Notifies (OVERLOAD) an invalidation with an existing event.
   */
  public void fireAreaInvalidated(FigureEvent event) {
    for (FigureListener listener : listenerList.getListeners(FigureListener.class)) {
      listener.areaInvalidated(event);
    }
  }

  /**
   * Notifies a figure removal request.
   */
  public void fireFigureRequestRemove(Rectangle2D.Double bounds) {
    fireFigureEvent(
        (listener, event) -> listener.figureRequestRemove(event),
        () -> new FigureEvent(figure, bounds));
  }

  /**
   * Notifies that the figure has been added to the drawing.
   */
  public void fireFigureAdded(Rectangle2D.Double bounds) {
    fireFigureEvent(
        (listener, event) -> listener.figureAdded(event), () -> new FigureEvent(figure, bounds));
  }

  /**
   * Notifies that the figure has been removed from the drawing.
   */
  public void fireFigureRemoved(Rectangle2D.Double bounds) {
    fireFigureEvent(
        (listener, event) -> listener.figureRemoved(event), () -> new FigureEvent(figure, bounds));
  }

  /**
   * Notifies that the figure has changed.
   */
  public void fireFigureChanged(Rectangle2D.Double changedArea) {
    fireFigureEvent(
        (listener, event) -> listener.figureChanged(event),
        () -> new FigureEvent(figure, changedArea));
  }

  /**
   * Notifies a change with an existing event.
   */
  public void fireFigureChanged(FigureEvent event) {
    fireFigureEvent((listener, evt) -> listener.figureChanged(evt), () -> event);
  }

  /**
   * Notifies an attribute change.
   */
  public <T> void fireAttributeChanged(AttributeKey<T> attribute, T oldValue, T newValue) {
    fireFigureEvent(
        (listener, event) -> listener.attributeChanged(event),
        () -> new FigureEvent(figure, attribute, oldValue, newValue));
  }

  /**
   * Notifies that the figure handles have changed.
   */
  public void fireFigureHandlesChanged(Rectangle2D.Double boundsArea) {
    fireFigureEvent(
        (listener, event) -> listener.figureHandlesChanged(event),
        () -> new FigureEvent(figure, boundsArea));
  }

  /**
   * Notifies an undoable edit to the parent drawing.
   */
  public void fireUndoableEditHappened(UndoableEdit edit) {
    if (drawing != null) {
      drawing.fireUndoableEditHappened(edit);
    }
  }

  /** tool method to process a listener and create its event object lazily. */
  private void fireFigureEvent(
      BiConsumer<FigureListener, FigureEvent> listenerConsumer,
      Supplier<FigureEvent> eventSupplier) {
    if (listenerList.getListenerCount() == 0) {
      return;
    }
    FigureEvent event = null;
    for (FigureListener listener : listenerList.getListeners(FigureListener.class)) {
      if (event == null) {
        event = eventSupplier.get();
      }
      listenerConsumer.accept(listener, event);
    }
  }

  public int getListenerCount() {
    return listenerList.getListenerCount();
  }

  public CompositeFigureListener[] getListeners(Class<CompositeFigureListener> class1) {
    return listenerList.getListeners(class1);
  }

  public void remove(Class<CompositeFigureListener> class1, CompositeFigureListener listener) {
    listenerList.remove(class1, listener);
  }

  public void add(Class<CompositeFigureListener> class1, CompositeFigureListener listener) {
    listenerList.remove(class1, listener);
  }

  public Object[] getListenerList() {
    return listenerList.getListenerList();
  }
}
