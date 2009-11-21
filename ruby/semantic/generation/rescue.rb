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
    #bgn(@blockName,["*"],@block.code,@block.value) +
    #bgn(@rescueName,["*"],@rescueBlock.code,@rescueBlock.value)+
    #s("#{@ret}=secured #{@blockName} #{@rescueName}")

    s("try")+
    (
    @block.code+
    s("@ret=#{@block.value}")).indent+
    @rescueBlocks.map{|b|

      b.code+
      s("#{@ret}=#{b.value}").indent+
      b.after.indent
    }+
    s("end")

  end

  def value
    @ret
  end
end