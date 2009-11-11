class RescueCode
  def init
    @blockName=tmpVar
    @rescueName=tmpVar
    @ret=tmpVar
    #pp self
    #exit
  end

  def before
    []
  end

  def after
    s("clear #{@blockName}")+
    s("clear #{@rescueName}")
  end

  def code
    bgn(@blockName,["*"],@block.code,@block.value) +
    bgn(@rescueName,["*"],@rescueBlock.code,@rescueBlock.value)+
    s("#{@ret}=secured #{@blockName} #{@rescueName}")
  end
  
  def value
    @ret
  end
end