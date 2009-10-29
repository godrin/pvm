class CallCode
  def init
    assert{filename}
    @ret=tmpVar
  end
  def before
    []
  end
  def after
    s("clear #{@ret}")
  end
  def code
    #pp self
    #exit
    s("#{@ret}=#{@name}(#{@args.value})")
  end
  def value
    @ret
  end
end