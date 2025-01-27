package fun.lykorisr.commitlint

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.gradle.api.InvalidUserDataException

class CommitlintUtilTest {
  CommitlintUtil util = CommitlintUtil.instance
  
  @Test 
  public void validMessageTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
 Duis aute irure dolor in reprehenderit in voluptate velit esse cillum

 dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
 proident, sunt in culpa qui officia deserunt mollit anim id 
est laborum."""
    util.validate(msg, false)
  }

  @Test 
  public void invalidCommitTypeTest() {
    final String msg = """invalid: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 """
    assertThrows(InvalidUserDataException){ 
      util.validate(msg, false)
    }
  }
  
  @Test 
  public void longSubjectTest() {
    final String msg = """chore: Lorem ipsum dolor sit amet consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    def ex = assertThrows(InvalidUserDataException){ 
      util.validate(msg, false)
    }
    
    assertEquals(util.E_LONG_SUBJECT, ex.getMessage())
  }
  
  @Test 
  public void noBlankLineAfterSubjectTest() {
    final String msg = """chore: Lorem ipsum dolor sit amet 
consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    def ex = assertThrows(InvalidUserDataException) { 
      util.validate(msg, false)
    }
    assertEquals(ex.getMessage(), util.E_NO_BLANK_LINE)
  }
  
  @Test 
  public void longLineTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure
 dolor in reprehenderit in voluptate velit esse cillum

 dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
 proident, sunt in culpa qui officia deserunt mollit anim id 
est laborum."""
    
    def ex = assertThrows(InvalidUserDataException) { 
      util.validate(msg, false)
    }
    assertEquals(ex.getMessage(), util.E_LONG_LINE)
  }
  
  @Test 
  void ignoreSemverTest() {
    final String msg1 = """chore: 1.1.1

Lorem ipsum dolor sit amet consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    final String msg2 = "1.2.3"

    util.validate(msg1, false)
    util.validate(msg2, false)
  }

  @Test 
  void ignoreMergeCommitsTest() {
    final String msg1 = """Merge branch 'feature/publish-to-github-packages' into 'master'

chore: publish to github packages

See merge request common/commitlint-plugin!2
"""
    final String msg2 = """Merged 'develop' into 'master'"""

    util.validate(msg1, false)
    util.validate(msg2, false)
  }

  @Test 
  void invalidMergeCommitsTest() {
    final String msg1 = """Lorem ipsum dolor Merge branch 'feature/publish-to-github-packages' into 'master'

chore: publish to github packages

See merge request common/commitlint-plugin!2
"""
    final String msg2 = """Merge 'develop' to 'master'"""

    def ex1 = assertThrows(InvalidUserDataException){ 
      util.validate(msg1, false)
    }
    def ex2 = assertThrows(InvalidUserDataException){ 
      util.validate(msg2, false)
    }
  }

  @Test
  void ignoreSquashAndFixupTest() {
    final String msg1 = """fixup! feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    
    final String msg2 = """squash! feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    
    final String msg3 = """blablabla! feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore"""
    
    util.validate(msg1, false)
    util.validate(msg2, false)
    assertThrows(InvalidUserDataException){ 
      util.validate(msg3, false)
    }
  }
  
  @Test 
  void commentedLineTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

# consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
 # exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum

 dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
 proident, sunt in culpa qui officia deserunt mollit anim id 
est laborum."""
    
    util.validate(msg, false)
  }

  @Test 
  void validMessageWithRefsTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
 Duis aute irure dolor in reprehenderit in voluptate velit esse cillum

refs #42"""

    util.validate(msg, true)
  }

  @Test 
  void messageWithoutRefsTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
 Duis aute irure dolor in reprehenderit in voluptate velit esse cillum

 dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
 proident, sunt in culpa qui officia deserunt mollit anim id 
est laborum."""
    
    assertThrows(InvalidUserDataException){
      util.validate(msg, true)
    }
  }

  @Test 
  void messageWithoutRefsNumberTest() {
    final String msg = """feat: Lorem ipsum dolor sit amet

consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
 et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
 Duis aute irure dolor in reprehenderit in voluptate velit esse cillum

refs #"""

    assertThrows(InvalidUserDataException){
      util.validate(msg, true)
    }
  }

  
}
