package org.camunda.bpm.getstarted.loanapproval

import org.camunda.bpm.engine.delegate.{DelegateExecution, JavaDelegate}
import play.api.Logger

class ProcessRequestDelegate extends JavaDelegate {

  def execute(execution: DelegateExecution): Unit = {
    val amount = execution.getVariable("amount")
    Logger.info(s"Processing loan approval for amount $amount...")
  }
}
