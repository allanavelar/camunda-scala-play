import org.camunda.bpm.engine.{ProcessEngines, ProcessEngineConfiguration}
import play.api.{Play, Application, GlobalSettings, Logger}

object Global extends GlobalSettings {

  /**
   * Start the camunda BPM process engine and deploy process definitions on start-up.
   */
  override def onStart(application: Application): Unit = {
    Play.configuration(application).getConfig("camunda").map { camundaConfiguration =>

      val configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
        .setJdbcDriver(camundaConfiguration.getString("jdbcDriver").getOrElse(sys.error("jdbcDriver undefined")))
        .setJdbcUrl(camundaConfiguration.getString("jdbcUrl").getOrElse(sys.error("jdbcUrl undefined")))
        .setJdbcUsername(camundaConfiguration.getString("jdbcUsername").getOrElse(sys.error("jdbcUsername undefined")))
        .setJdbcPassword(camundaConfiguration.getString("jdbcPassword").getOrElse(sys.error("jdbcPassword undefined")))
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
        .setHistory(ProcessEngineConfiguration.HISTORY_FULL)
        .setJobExecutorActivate(true)

      Logger.info("Starting process engine...")
      val engine = configuration.buildProcessEngine()

      camundaConfiguration.getString("processDefinition").map { processDefinition =>
        Logger.info("Deploying process definition...")
        val deployment = engine.getRepositoryService.createDeployment()
        deployment.addClasspathResource(processDefinition).enableDuplicateFiltering(true).deploy()
      }.getOrElse(Logger.warn("No process definition configured."))

    }.getOrElse(Logger.error("camunda configuration not found."))
  }

  override def onStop(application: Application): Unit = {
    ProcessEngines.getDefaultProcessEngine.close()
  }
}
