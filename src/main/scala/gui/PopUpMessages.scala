package gui

import javax.swing.JOptionPane

import paths.PathKeeper

import scala.swing.MainFrame

class PopUpMessages(top: MainFrame, pathKeeper: PathKeeper) {

  def wantToCloseFile(): Boolean = {
    if (! pathKeeper.saved && ! pathKeeper.isnew) {
      val choice = JOptionPane.showOptionDialog(
        top.peer,
        "You made changes in your file.\nAre you sure you want to close it?",
        "File is not saved",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        null,
        null)
      if (choice == JOptionPane.YES_OPTION) {
        true
      }
      else false
    }
    else true
  }

  def wantToQuit(): Boolean = {
    if (!pathKeeper.saved && !pathKeeper.isnew) {
      val choice = JOptionPane.showOptionDialog(
        top.peer,
        "You made changes in your file.\nAre you sure you want to Quit?",
        "Really Quit?",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        null,
        null)
      if (choice == JOptionPane.YES_OPTION) {
        true
      }
      else false
    }
    else true
  }

}
