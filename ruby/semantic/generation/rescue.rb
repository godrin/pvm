class RescueCode
  def init
    @blockName=tmpVar
    @rescueName=tmpVar
    #pp self
    #exit
  end

  def before
    []
  end

  def after
    []
  end

  def code
    s("#{@blockName}=begin()")+@block.code.indent+s("end")+
    s("#{@rescueName}=begin()")+@rescueBlock.code.indent+s("end")+
    s("secured #{@blockName} #{@rescueName}")+
    s("clear #{@blockName}")+
    s("clear #{@rescueName}")
  end
end