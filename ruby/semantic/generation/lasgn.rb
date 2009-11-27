class LasgnCode
  def before
    []
  end

  def after
    []
  end

  def code
    if @value.nil?
      s(@name.to_s)
    else
      @value.before+
      @value.code+
      s("#{@name}=#{@value.value}")+
      @value.after
    end
  end

  def value
    @name.to_s
  end
  
  def as_parameters
    @name.to_s
  end
end