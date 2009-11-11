class YieldCode
  def init
    @t=tmpVar
    @f=tmpVar
  end

  def value
    @t
  end

  def before
    s("#{@f}=self.getBlock")+
    s("#{@t}=#{@f}(#{@args.map{|a|a.value}.join(",")})")+
    s("clear #{@f}")
  end
  def after
    s("clear #{@t}")
  end
end