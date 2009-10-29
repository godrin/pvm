class ClassCode
  def init
    @tmpModuleName=tmpVar
  end

  def before
    []
  end

  def code
    #  pp self
    #  exit
    if @parent

      @parent.before+
      s("#{@tmpModuleName}=VMKlass.newKlass(#{@parent.value})")+
      s("#{@name}=#{@tmpModuleName}")+
      @parent.after+
      @body.code
    else
      s("#{@tmpModuleName}=VMKlass.newKlass()")+
      s("#{@name}=#{@tmpModuleName}")+
      @body.code
    end

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