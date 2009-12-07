class WhileCode
  def init
    @valName=tmpVar
    @blockName=tmpVar
    @condVal=tmpVar

  end

  def before
    []
  end

  def code
    condCode=@cond.code+
    s("#{@condVal}=#{@cond.value}")

    condCode+
    bgn(@blockName,["*"],@block.code+
    s("#{@valName}=#{@block.value}")+
    condCode)+
    s("while #{@condVal} do #{@blockName}")
  end

  def after
    s("clear #{@valName}")+
    s("clear #{@blockName}")+
    s("clear #{@condVal}")
  end

  def value
    @valName
  end

end