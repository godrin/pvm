class ModuleCode
  def init
    @tmpModuleName=tmpVar
  end
  def before
    []
  end
  
  def code
    s("#{@tmpModuleName}=VMModule.newModule()")+
    s("#{@name}=#{@tmpModuleName}")+
    @body.code
  end
  
  def value
    @name
  end
  
  def after
    s("clear #{@tmpModuleName}")
  end
  
  def curModule
    @tmpModuleName
  end
end